<html>
<head>
    <title>Articulate - Where articles express</title>
    <link rel="stylesheet" type="text/css" href="http://localhost:8011/static/web/css/articulate_main_page.css">
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

	<form action="/analyze" method="POST">
		
		<div class="wrapper">
			<div class="article">
				<input type="text" class="title" name="article-title-1" placeholder="Article Tttle Here ..." /><br/>
	      		<textarea name="article-body-1" placeholder="Article Here ..." type="text" class="body" id=""></textarea>
	      	</div>
      	</div>
      	<div class="line" ></div>

      	<div class="wrapper">
			<div class="article">
				<input type="text" class="title" name="article-title-2" placeholder="Article Tttle Here ..." /><br/>
	      		<textarea name="article-body-2" placeholder="Article Here ..." type="text" class="body" id=""></textarea>
	      	</div>
      	</div>
      	<div class="line" ></div>

      	<div class="wrapper">
			<div class="article">
				<input type="text" class="title" name="article-title-3" placeholder="Article Tttle Here ..." /><br/>
	      		<textarea name="article-body-3" placeholder="Article Here ..." type="text" class="body" id=""></textarea>
	      	</div>
      	</div>
      	<div class="line" ></div>

      	<div class="wrapper">
			<div class="article">
				<input type="text" class="title" name="article-title-4" placeholder="Article Tttle Here ..." /><br/>
	      		<textarea name="article-body-4" placeholder="Article Here ..." type="text" class="body" id=""></textarea>
	      	</div>
      	</div>
      	<div class="line" ></div>

      	<div class="wrapper">
			<div class="article">
				<input type="text" class="title" name="article-title-5" placeholder="Article Tttle Here ..." /><br/>
	      		<textarea name="article-body-5" placeholder="Article Here ..." type="text" class="body" id=""></textarea>
	      	</div>
      	</div>
      	<div class="line" ></div>

      	<input type="submit" />


	</form>

</body>
</html>
