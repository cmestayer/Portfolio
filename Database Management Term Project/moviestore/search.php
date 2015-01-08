<?php
session_start(); //start session
include("db_access.php");
$return_url = base64_decode($_POST["return_url"]); //return url

if (isset($_GET["delete"]))
{
	$return_url = base64_decode($_GET["return_url"]); //return url
    unset($_SESSION['Search']);
    header('Location:'.$return_url);
}


if (isset($_POST["keywords"]))
{
	//search by keywords
	$keyword = filter_var($_POST["keywords"], FILTER_SANITIZE_STRING);
	$results = $db->query("SELECT p_no FROM movie m, product p WHERE p.[m_no] = m.[m_no] AND (m.[title] like '%$keyword%' OR m.[director] like '%$keyword%') LIMIT 1");
	if ($results){
		$obj = $results->fetchObject();
		$search = array();
		foreach ($obj as $s_itm) //loop through array
        {
			$search['pno'] = ($s_itm["p_no"]);  
        }
		$_SESSION["Search"] = $search;
	}
	else{
	$_SESSION["Search"] = "No items found";
	}
}

if (isset($_POST["disc_type"]))
{
	//search by disc_type
	$disc = $_POST["disc"];
	$results = $db->query("SELECT p_no FROM product WHERE disc_type = '$disc[0]' LIMIT 1");
	if ($results){
		$obj = $results->fetchObject();
		$search = array();
		foreach ($obj as $s_itm) //loop through array
        {
			$search['pno'] = ($s_itm["p_no"]);  
        }
		$_SESSION["Search"] = $search;
	}
	else{
	$_SESSION["Search"] = "No items found";
	}
}

if (isset($_POST["genre"]))
{
	//search by genre
	$gen = $_POST["genre"];
	$results = $db->query("SELECT p_no FROM movie m, product p WHERE p.[m_no] = m.[m_no] AND m.[genre] = '$gen[0]' LIMIT 1");
	if ($results){
		$obj = $results->fetchObject();
		$search = array();
		foreach ($obj as $s_itm) //loop through array
        {
			$search['pno'] = ($s_itm["p_no"]);  
        }
		$_SESSION["Search"] = $search;
	}
	else{
	$_SESSION["Search"] = "No items found";
	}
}
header('Location:'.$return_url);
?>