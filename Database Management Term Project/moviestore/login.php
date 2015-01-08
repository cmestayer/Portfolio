<?php
include("db_access.php");


	$co_email   = filter_var($_POST["email"], FILTER_SANITIZE_STRING); //product code
    $co_password    = filter_var($_POST["password"], FILTER_SANITIZE_STRING); //product qty
	$return_url = base64_decode($_POST["return_url"]);
	
	$query = $db->prepare("SELECT email, password, c_no FROM customer WHERE email = '$co_email' AND password = '$co_password'"); //prepares the query
	$query->execute();
	if($query){
		$cus = $query->fetchObject();
	
		// Check if username and password are correct
		if ($_POST["email"] == $cus->email && $_POST["password"] == $cus->password){ 
			$customerID = $cus->c_no;
			// If correct, we set the session to YES
			session_start();
			$_SESSION["Login"] = c_no;
			//echo "<h1>You are now logged correctly in</h1>";
			//echo "<p><a href='store.php'>Visit the store!!</a><p/>";
	 
		}else{
	 
		// If not correct, we set the session to NO
		session_start();
		$_SESSION["NOTLogin"];
		//echo "<h1>You are NOT logged correctly in </h1>";
		//echo "<p><a href='form.php'>Please re log in</a></p>";
		}
	}else{
	echo '<p> error</p>';
	}
	header('Location:'.$return_url);
	
?>