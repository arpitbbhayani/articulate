<html>
<head>
    <title>Articulate - Where articles express</title>
    <link rel="stylesheet" type="text/css" href="http://localhost:8011/static/web/css/entity_analysis.css">
</head>
<body>

	<header>
		<div id="header_links">
			<ul>
				<a href="#"><li>About Us</li></a><li>|</li>
				<a href="#"><li>How To</li></a><li>|</li>
				<a href="#"><li>Contact Us</li></a>
			</ul>
		</div>
		<a href="#"><img src="http://localhost:8011/static/web/images/article.png"/></a>
		<a href="#"><h1>Articulate</h1></a>
	</header>

	<div id="page">

	    <#list map?keys as article>

		<div class="article">

			<#if article != "OUTPUT">
			    <h2>Article: <span class="italic">${article}</span></h2>
			    <h3>Statistics: </h3>
            <#else>
                <h2><span class="italic">Interestingness measure of articles.</span></h2>
			</#if>

			<#list map[article]?keys as class>

			    <#if article != "OUTPUT">
                    <h4>Class: <span class="italic">${class}</span></h4>
                <#else>
                    <h4><span class="italic">${class}</span></h4>
                </#if>

                <h5>
                    <#list map[article][class]?keys as word>
                        ${word},
                    </#list>
                </h5>
			</#list>
		</div>

		<div class="line"></div>

	    </#list>

	</div>

    <br/><br/><br/><br/><br/><br/>

</body>
</html>