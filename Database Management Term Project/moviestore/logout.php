<?php 
session_start();
	if(isset($_SESSION['Login'])){
		unset($_SESSION['Login']);
	}
	$_SESSION["NOTLogin"];
header('Location:store.php');
?>