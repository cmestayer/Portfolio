<?php
session_start();
include("db_access.php");
$current_url = base64_encode("http://".$_SERVER['HTTP_HOST'].$_SERVER['REQUEST_URI']);
?>
<head>
<link rel="stylesheet" type="text/css" href="store.css">
</head>


<?php if (isset($_POST["clear"])){
				clearSearch();
			}
?>

<table>
<tr>
<td colspan="3">
<h1> Welcome to the DVD/Blu-Ray Store</h1><br><br>
</td>
</tr>
<tr>
<td style="width:100px;vertical-align:top;">
<?php
if(isset($_SESSION["Login"])){
echo '<p>Logged in</p>';
echo '<form method = "post" action="logout.php">
		<input type="submit" value="LOG OUT"/></form>';
}else{
 echo '<h2> Please login</h2>
	<form method="post" action="login.php">

	<p>Email: <input type="text" name="email" /></p>
	<p>Password: <input type="password" name="password" /></p>
	<input type="hidden" name="return_url" value="'.$current_url.'" />

	<p><input type="submit" value="Let me in" /></p>
	</form><br>';
	echo "<a href='create_account.php'>Create Account</a><br>";
	}
	echo'<form method="post" action="search.php">';
	echo'<p>Search Keywords: <input type="text" name="keywords"/></p>';
	echo'<input type="submit" value="Search" name="search_submit">';
	echo '<input type="hidden" name="return_url" value="'.$current_url.'" /></form>';
	?>
	<form action="search.php" method="post">
	<select name="genre[]" size="1" multiple="multiple">
		<?php 
		$sql = "SELECT DISTINCT genre FROM movie ORDER BY genre";
		$query = $db->prepare($sql);
		$query->execute();
		$rows = $query->fetchAll();
		foreach($rows as $row){
		$choice = $row["genre"];
		?>
			<option value="<?= $choice ?>"><?= $choice ?></option>
		<?PHP
		}
		?>
    </select>
	<input type="submit" value="Search" name="genre_submit">
	<input type="hidden" name="return_url" value="<?= $current_url ?>" /></form>
	</form>
	<form action="search.php" method="post">
	<select name="disc[]" size="1" multiple="multiple">
		<?php 
		$sql = "SELECT DISTINCT disc_type FROM product ORDER BY disc_type";
		$query = $db->prepare($sql);
		$query->execute();
		$rows = $query->fetchAll();
		foreach($rows as $row){
		$choice = $row["disc_type"];
		?>
			<option value="<?= $choice ?>"><?= $choice ?></option>
		<?PHP
		}
		?>
    </select>
	<input type="submit" value="Search" name="disc_submit">
	<input type="hidden" name="return_url" value="<?= $current_url ?>" /></form>
	</form>
	
<br>

</td>
<td style="height:100%;width:100%;vertical-align:top;">
<div class="products">
<?php

    
	$sql = "SELECT title, director, genre, disc_type, price, p_no, qty
	FROM movie m, product p
	WHERE m.[m_no] = p.[m_no]";
	$query = $db->prepare($sql); //prepares the query
	$query->execute();
    if ($query) { 
        //output results from database
		if (isset($_SESSION["Search"]))
		{
			echo '<form action="store.php" method="post">';
			echo '<input type="submit" value="Clear Search" name="clear">';
			if ( $_SESSION["Search"] == "No items found")
			{
			echo '<p>No items found on your search</p>';
			}
			while($obj = $query->fetchObject()){
				foreach($_SESSION["Search"] as $s_itm)
				{
					if($s_itm["p_no"] == $obj->p_no){
						printMovies($obj);
					}
				}
			}
			
		}
		else{
			while($obj = $query->fetchObject()){
				printMovies($obj);
			}
        }
		
    
}
?>
</div>
<td style="vertical-align:top;">
<div class="shopping-cart">
<h2>Your Shopping Cart</h2>
<?php
if(isset($_SESSION["products"]))
{
    $total = 0;
    echo '<ol>';
    foreach ($_SESSION["products"] as $cart_itm)
    {
        echo '<li class="cart-itm">';
        echo '<span class="remove-itm"><a href="cart_update.php?removep='.$cart_itm["code"].'&return_url='.$current_url.'">&times;</a></span>';
        echo '<h3>'.$cart_itm["title"].'</h3>';
        echo '<div class="p-code">P code : '.$cart_itm["code"].'</div>';
        echo '<div class="p-qty">Qty : '.$cart_itm["qty"].'</div>';
        echo '<div class="p-price">Price : $'.$cart_itm["price"].'</div>';
        echo '</li>';
        $subtotal = ($cart_itm["price"]*$cart_itm["qty"]);
        $total = ($total + $subtotal);
    }
    echo '</ol>';
    echo '<span class="check-out-txt"><strong>Total : $'.$total.'</strong> <a href="view_cart.php">Check-out!</a></span>';
    echo '<span class="empty-cart"><a href="cart_update.php?emptycart=1&return_url='.$current_url.'"> Empty Cart</a></span>';
}else{
    echo 'Your Cart is empty';
}
?>
</div>
</td>
</tr>

<tr>
<td colspan="3" style="text-align:center;vertical-align:bottom;">
blah blah blah</td>
</tr>
</table>

<?php
function printMovies($obj)
{
	$current_url = base64_encode("http://".$_SERVER['HTTP_HOST'].$_SERVER['REQUEST_URI']);
	echo '<div class="product">'; 
    echo '<form method="post" action="cart_update.php">';
    echo '<div class="product-content"><h3>'.$obj->title.'</h3>';
    echo '<div class="product-director">'.$obj->director.'</div>';
	echo '<div class="product-genre">'.$obj->genre.'</div>';
	echo '<div class="product-disc">'.$obj->disc_type.'</div>';
    echo '<div class="product-info">Price $'.$obj->price.'<button class="add_to_cart">Add To Cart</button></div>';
    echo '</div>';
	echo '<input type="number" name="product_qty" value="1" min="1" max="'.$obj->qty.'">';
    echo '<input type="hidden" name="product_code" value="'.$obj->p_no.'" />';
    echo '<input type="hidden" name="type" value="add" />';
    echo '<input type="hidden" name="return_url" value="'.$current_url.'" />';
    echo '</form>';
    echo '</div>';
}

function clearSearch()
{
	unset($_SESSION["Search"]);
}
?>