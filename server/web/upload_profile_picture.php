<?php
$user = $_POST["user"];
$info = pathinfo($_FILES["pic"]["name"]);
$targetFile = "uploads/profile_pictures/" . $user . "." . $info["extension"];
if(move_uploaded_file($_FILES["pic"]["tmp_name"], $targetFile))
    echo "!" . $_SERVER['SERVER_NAME'] . dirname($_SERVER['REQUEST_URI']) . "/" . $targetFile;
else
    echo "101";
?>