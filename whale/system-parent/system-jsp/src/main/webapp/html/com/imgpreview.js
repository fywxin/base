/*
 * imgPreview jQuery plugin
 * Copyright (c) 2009 James Padolsey
 * j@qd9.co.uk | http://james.padolsey.com
 * Dual licensed under MIT and GPL.
 * Updated: 09/02/09
 * @author James Padolsey
 * @version 0.22
 */
(function($){
    $.expr[':'].linkingToImage = function(elem, index, match){
        // This will return true if the specified attribute contains a valid link to an image:
        return !! ($(elem).attr(match[3]) && $(elem).attr(match[3]).match(/\.(gif|jpe?g|png|bmp)$/i));
    };
    
    $.fn.imgPreview = function(userDefinedSettings){
        var s = $.extend({
            // CSS to be applied to image:
            imgCSS: {"border": "2px solid #BFCDDB"},
            // Distance between cursor and preview:
            distanceFromCursor: {top:10, left:10},
            // Boolean, whether or not to preload images:
            preloadImages: false,
            // Callback: run when link is hovered: container is shown:
            onShow: function(){},
            // Callback: container is hidden:
            onHide: function(){},
            // Callback: Run when image within container has loaded:
            onLoad: function(){},
            // ID to give to container (for CSS styling):
            containerID: 'imgPreviewContainer',
            // Class to be given to container while image is loading:
            containerLoadingClass: 'loading',
            // Prefix (if using thumbnails), e.g. 'thumb_'
            thumbPrefix: '',
            // Where to retrieve the image from:
            srcAttr: 'src'
            
        }, userDefinedSettings),
        
        $container = $('<div/>').attr('id', s.containerID)
                        .append('<img/>').hide()
                        .css('position','absolute')
                        .appendTo('body'),
            
        $img = $('img', $container).css(s.imgCSS);
        
        // Re-usable means to add prefix (from setting):
        function addPrefix(src) {
        	return src;
            //return src.replace(/(\/?)([^\/]+)$/,'$1' + s.thumbPrefix + '$2');
        }
        
        if($(this).attr("mark") == "img"){
        	$(this).mousemove(function(e){
             $container.css({
                 top: e.pageY + s.distanceFromCursor.top + 'px',
                 left: e.pageX + s.distanceFromCursor.left + 'px'
             });
        }).hover(function(){
             var link = this;
             $container.addClass(s.containerLoadingClass).show();
             $img.load(function(){
                     $container.removeClass(s.containerLoadingClass);
                     $img.show();
                     s.onLoad.call($img[0], link);
                 	}).attr( 'src' , addPrefix($(link).attr(s.srcAttr)) );
             s.onShow.call($container[0], link);
            }, function(){
                $container.hide();
                $img.unbind('load').attr('src','').hide();
                s.onHide.call($container[0], this);
                
            });
        }
        return this;
    };
})(jQuery);

function show(w){
	$(w).imgPreview();
	return $(w);
}
