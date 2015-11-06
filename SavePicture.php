<?php
	$name = $_POST["name"];
	$image = $_POST["image"];

	$decodedImage = base64_decode("$image");
	#file_put_contents("pictures/". $name . ".JPG", $decodedImage)
	file_put_contents("pictures/". $name . ".mp3", $decodedImage)
?>