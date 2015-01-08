<?php
include("db_access.php");

$sql = "Select email, c_no From customer";
$query = $db->prepare($sql);
$query->execute();
while($obj = $query->fetchObject())
{
	if($_POST["Email"] == $obj->email)
	{
		$return_url = base64_decode($_POST["return_url"]);
		header('Location:'.$return_url);
	}
	$c = $obj->c_no;
}
	$c = $c + 1;
		$sql = "INSERT INTO customer(c_no, fname, lname, street, city, state, zip, phone, email, password) 
		values('".$c."','" . $_POST["FirstName"] . "','" . $_POST["LastName"] . "','" . $_POST["Street"] . "',
		'" . $_POST["City"] . "','" . $_POST["State"] . "','" . $_POST["Zip"] . "',
		'" . $_POST["Phone"] . "','" . $_POST["Email"] . "','" . $_POST["Password"] . "')";
		$query = $db->prepare($sql); //prepares the query
		$query->execute();
		session_start();
		$_SESSION["Login"] = $c;
		header('Location:store.php');
?>