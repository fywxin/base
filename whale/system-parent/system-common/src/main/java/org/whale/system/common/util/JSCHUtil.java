package org.whale.system.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.system.common.exception.ScpFileException;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * 效率出现问题时，或者功能不满足时，换成 ganymed-ssh2
 * 
 * @author 王金绍
 * @date 2015年11月29日 下午6:05:34
 */
public class JSCHUtil {
	private static final Logger log = LoggerFactory.getLogger(JSCHUtil.class
			.getName());
	private Session session;
	private ChannelShell channelShell;
	private ChannelSftp channelSftp;
	private StringBuffer buffer = new StringBuffer();

	public static final int COMMAND_EXECUTION_SUCCESS_OPCODE = -2;
	public static final String BACKSLASH_R = "\r";
	public static final String BACKSLASH_N = "\n";
	public static final String COLON_CHAR = ":";
	public static String ENTER_CHARACTER = BACKSLASH_R;
	public static final int SSH_PORT = 22;

	// 正则匹配，用于处理服务器返回的结果
	public static String[] linuxPromptRegEx = new String[] { "~]#", "~#", "#",
			":~#", "/$", ">" };

	public static String[] errorMsg = new String[] { "could not acquire the config lock " };

	/**
	 * 获取服务器返回的信息
	 * 
	 * @return 服务端的执行结果
	 */
	public String getResponse() {
		return buffer.toString();
	}

	/**
	 * 关闭SSH远程连接
	 */
	public void disconnect() {
		if (channelShell != null) {
			channelShell.disconnect();
		}
		if (channelSftp != null) {
			channelSftp.disconnect();
		}
		if (session != null) {
			session.disconnect();
		}
	}

	/**
	 * 连接会话
	 * 
	 * @param ip
	 *            主机IP
	 * @param user
	 *            主机登陆用户名
	 * @param psw
	 *            主机登陆密码
	 * @param port
	 *            主机ssh2登陆端口，如果取默认值，传-1
	 */
	public void connectSession(String ip, String user, String psw, int port)
			throws Exception {
		JSch jsch = new JSch();

		if (port <= 0) {
			// 连接服务器，采用默认端口
			session = jsch.getSession(user, ip);
		} else {
			// 采用指定的端口连接服务器
			session = jsch.getSession(user, ip, port);
		}

		// 如果服务器连接不上，则抛出异常
		if (session == null) {
			throw new Exception("session is null");
		}

		// 设置登陆主机的密码
		session.setPassword(psw);// 设置密码
		// 设置第一次登陆的时候提示，可选值：(ask | yes | no)
		session.setConfig("StrictHostKeyChecking", "no");
		// 设置登陆超时时间
		session.connect(30000);
	}

	/**
	 * 建立SFTP连接
	 * 
	 * @throws Exception
	 */
	public void connectSftp() throws Exception {
		if (session == null) {
			throw new ScpFileException("session is null");
		}
		// 创建sftp通信通道
		channelSftp = (ChannelSftp) session.openChannel("sftp");
		channelSftp.connect(3000);
	}
	
	/**
	 * 删除文件
	 * @param file
	 * @throws SftpException
	 */
	public void delFile(String file) throws SftpException{
		channelSftp.rm(file);
	}
	
	/**
	 * 文件上传复制
	 * @param instream
	 * @param deployDir
	 * @param fileName
	 * @throws SftpException
	 * @throws IOException
	 */
	public void doScpStream(InputStream instream, String deployDir, String fileName) throws SftpException, IOException{
		OutputStream outstream = null;
		// 进入服务器指定的文件夹
		try {
			channelSftp.cd(deployDir);
			log.info("scp file " + fileName);
			outstream = channelSftp.put(fileName);
			byte[] data = IOUtils.toByteArray(instream);
			IOUtils.write(data, outstream);
			outstream.flush();
		} finally {
			IOUtils.closeQuietly(outstream);
			IOUtils.closeQuietly(instream);
		}
	}

	/**
	 * 复制文件或者目录
	 * 
	 * @param file
	 *            本地文件对象
	 * @param deployDir
	 *            服务器部署目录
	 * @throws SftpException 
	 * @throws IOException 
	 * @throws Exception
	 */
	public void scpStream(InputStream instream, String deployDir,
			String fileName) throws SftpException, IOException {
		if (channelSftp == null)
			throw new ScpFileException("channelSftp is null , please call connectSftp()");
		try {
			this.doScpStream(instream, deployDir, fileName);
		} catch (Exception e) {
			this.mkDirs(deployDir);
			this.doScpStream(instream, deployDir, fileName);
		}
	}
	
	/**
	 * 递归创建目录
	 * @param deployDir
	 * @throws SftpException
	 */
	private void mkDirs(String deployDir) throws SftpException{
		Vector<?> content= null;
		try{
			content= channelSftp.ls(deployDir);
		}catch(Exception e){
			log.error("", e);
		}
		
		if(content == null){
			List<String> paths = new ArrayList<String>();
			paths.add(deployDir);
			deployDir = deployDir.substring(1);
			String[] pathNames = deployDir.split("/");
			if(pathNames.length > 1){
				String path = "";
				for(int i= pathNames.length-2; i>=0; i--){
					for(int j=0; j<=i; j++){
						path += "/"+pathNames[j];
					}
					if(Strings.isNotBlank(path) && path.length() >1){
						paths.add(path);
					}
					path = "";
				}
			}
			
			boolean needLs = true;
			for(int i=paths.size()-1; i>=0; i--){
				if(needLs){
					try{
						content= channelSftp.ls(paths.get(i));
						System.out.println(paths.get(i));
						System.out.println(content);
						needLs = content != null;
					}catch(Exception e){
						needLs = false;
					}
				}
				if(!needLs){
					channelSftp.mkdir(paths.get(i));
				}
			}
		}
	}

	/**
	 * 复制文件或者目录
	 * 
	 * @param file
	 *            本地文件对象
	 * @param deployDir
	 *            服务器部署目录
	 * @throws Exception
	 */
	public void scpFile(File file, String deployDir) throws Exception {
		if (channelSftp == null)
			throw new ScpFileException(
					"channelSftp is null ,please call connectSftp()");
		this.mkDirs(deployDir);
		// 进入服务器指定的文件夹
		channelSftp.cd(deployDir);
		// 以下代码实现从本地上传一个文件到服务器，如果要实现下载，对换以下流就可以了
		if (file.isFile()) {
			OutputStream outstream = null;
			InputStream instream = null;
			try {
				log.info("scp file " + file.getName());
				outstream = channelSftp.put(file.getName());
				instream = new FileInputStream(file);

				byte b[] = new byte[102400];
				int n;
				while ((n = instream.read(b)) != -1) {
					outstream.write(b, 0, n);
				}
				outstream.flush();
			} catch (Exception e) {
				throw new ScpFileException(e);
			} finally {
				IOUtils.closeQuietly(outstream);
				IOUtils.closeQuietly(instream);
			}
		} else if (file.isDirectory()) {
			deployDir = deployDir + "/" + file.getName();
			channelSftp.mkdir(deployDir);
			log.info("mkdir " + deployDir);
			for (File file1 : file.listFiles()) {
				scpFile(file1, deployDir);
			}
		}
	}

	
	public static void main(String[] args) {
		String path = "/dfgsd\\dfsgsdf//sa";
		path = path.replaceAll("//", "/").replace("\\", "/");
		String[] ps = path.split("/");
		for(String s : ps) { 
			System.out.println(s);
		}
	}
}
