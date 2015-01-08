<?php
session_start();
include_once("db_access.php");
$current_url = base64_encode("http://".$_SERVER['HTTP_HOST'].$_SERVER['REQUEST_URI']);
    if((isset($_SESSION["products"]))&&isset($_SESSION["Login"]))
    {
        $total = 0;
        echo '<form method="post" action="purchase.php">';
        echo '<ul>';
        $cart_items = 0;
        foreach ($_SESSION["products"] as $cart_itm)
        {
			
           $product_code = $cart_itm["code"];
           $results = $db->query("SELECT title, price FROM movie m, product p WHERE p_no='$product_code' AND p.[m_no] = m.[m_no] LIMIT 1");
           $obj = $results->fetchObject();
		  // $sql = "INSERT INTO cart(item_no, p_no, ) Values('$cart_items','$product_code')";
			//$query = $db->prepare($sql); //prepares the query
			//$query->execute();
           
            echo '<li class="cart-itm">';
            echo '<span class="remove-itm"><a href="cart_update.php?removep='.$cart_itm["code"].'&return_url='.$current_url.'">&times;</a></span>';
            echo '<div class="p-price"> $'.$obj->price.'</div>';
            echo '<div class="product-info">';
            echo '<h3>'.$obj->title.' (Code :'.$product_code.')</h3> ';
            echo '<div class="p-qty">Qty : '.$cart_itm["qty"].'</div>';
            echo '</div>';
            echo '</li>';
            $subtotal = ($cart_itm["price"]*$cart_itm["qty"]);
            $total = ($total + $subtotal);

            echo '<input type="hidden" name="item_name['.$cart_items.']" value="'.$obj->title.'" />';
            echo '<input type="hidden" name="item_code['.$cart_items.']" value="'.$product_code.'" />';
            echo '<input type="hidden" name="item_qty['.$cart_items.']" value="'.$cart_itm["qty"].'" />';
            $cart_items ++;
            
        }
        echo '</ul>';
        echo '<span class="check-out-txt">';
        echo '<strong>Total : $'.$total.'</strong>  ';
        echo '</span>';
		echo '<input type="submit" name="check Out" value="checkout"/>';
        echo '</form>';
        
    }elseif(isset($_SESSION["products"])){
		echo 'Please Log in first';
		echo '<form method="post" action="login.php">

			<p>Email: <input type="text" name="email" /></p>
			<p>Password: <input type="password" name="password" /></p>
			<input type="hidden" name="return_url" value="'.$current_url.'" />

			<p><input type="submit" value="Let me in" /></p>
			</form><br>';
		echo "<a href='create_account.php'>or Create Account</a><br>";
	}else{
        echo 'Your Cart is empty';
    }
	
	function addItem($i_no, $p_code, $qty)
	{
	//Items are added to database
		//$sql = "INSERT INTO cart(item_no, p_no, qty) Values( '".$i_no."','".$p_code."','".$qty."')";
		//$query = $db->prepare($sql); //prepares the query
		//$query->execute();
	}
?>