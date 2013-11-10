<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!--
Design by Free CSS Templates
http://www.freecsstemplates.org
Released for free under a Creative Commons Attribution 2.5 License

Name       : Mongoose 
Description: A two-column, fixed-width design with dark color scheme.
Version    : 1.0
Released   : 20130920

-->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<t:header/>
<t:body>
    <jsp:attribute name="content">
            <div id="content">
                <div class="title">
                    <h2>Welcome to our website</h2>
                    <span class="byline">Mauris vulputate dolor sit amet nibh</span> </div>
                <a href="#" class="image image-full"><img src="images/pic01.jpg" alt="" /></a>
                <p>This is <strong>Mongoose </strong>, a free, fully standards-compliant CSS template designed by <a href="http://www.freecsstemplates.org/" rel="nofollow">FreeCSSTemplates.org</a>. The photos in this template are from <a href="http://fotogrph.com/"> Fotogrph</a>. This free template is released under a <a href="http://creativecommons.org/licenses/by/3.0/">Creative Commons Attributions 3.0</a> license, so you are pretty much free to do whatever you want with it (even use it commercially) provided you keep the links in the footer intact. Aside from that, have fun with it :) </p>
                <div id="onecolumn">
                    <div class="title">
                        <h2>Pellentesque quis lectus gravida blandit</h2>
                    </div>
                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas ac quam risus, at tempus justo. Sed dictum rutrum massa eu volutpat. Quisque vitae hendrerit sem.</p>
                </div>
            </div>
    <div id="sidebar">
        <ul class="style1">
            <li class="first">
                <h3>Amet sed volutpat mauris</h3>
                <p><a href="#">In posuere eleifend odio. Quisque semper augue mattis wisi. Pellentesque viverra vulputate enim. Aliquam erat volutpat.</a> Donec leo, vivamus fermentum nibh in augue praesent a lacus at urna congue rutrum.</p>
            </li>
            <li>
                <h3>Sagittis diam dolor sit amet</h3>
                <p><a href="#">In posuere eleifend odio. Quisque semper augue mattis wisi. Pellentesque viverra vulputate enim. Aliquam erat volutpat.</a> Donec leo, vivamus fermentum nibh in augue praesent a lacus at urna congue rutrum.</p>
            </li>
        </ul>
        <div id="stwo-col">
            <div class="sbox1">
                <h2>Etiam rhoncus</h2>
                <ul class="style2">
                    <li><a href="#">Semper quis egetmi dolore</a></li>
                    <li><a href="#">Quam turpis feugiat dolor</a></li>
                    <li><a href="#">Amet ornare hendrerit lectus</a></li>
                    <li><a href="#">Consequat lorem phasellus</a></li>
                    <li><a href="#">Amet turpis feugiat amet</a></li>
                </ul>
            </div>
            <div class="sbox2">
                <h2>Integer gravida</h2>
                <ul class="style2">
                    <li><a href="#">Semper quis egetmi dolore</a></li>
                    <li><a href="#">Quam turpis feugiat dolor</a></li>
                    <li><a href="#">Amet ornare hendrerit lectus</a></li>
                    <li><a href="#">Consequat lorem phasellus</a></li>
                    <li><a href="#">Amet turpis feugiat amet</a></li>
                </ul>
            </div>
        </div>
    </div>

    </jsp:attribute>
</t:body>
<t:footer/>
</html>
