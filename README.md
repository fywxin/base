base
====

整体架构图

![image](https://github.com/fywxin/base/blob/master/doc/img/architecture%3B.png)

异常解决：

```
java.lang.ClassNotFoundException: org.apache.jsp.index_jsp
```

jdk8+tomcat8+spring4

1. copy  jstl-1.2.jar 和 standard-1.1.2.jar 到tomcat8目录

2. idea配置tomcat时，加入如上两个包依赖

   ![image](https://github.com/fywxin/base/blob/master/doc/img/jsp.png)