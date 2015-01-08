<?php
include("db_access.php");
?>
<html>
	<body>

<?php
	if (!isset($_SESSION["Login"]))
	{
?>
		<h2>Create an Account</h2>
		<form action="add_account.php" method="post">
		<p>First Name:<input type="text" name="FirstName" /></p>
		<p>Last Name:<input type="text" name="LastName" /></p>
		<p>Street Address:<input type="text" name="Street" /></p>
		<p>City:<input type="text" name="City" /></p>
		<p>ZIP:<input type="number" name="Zip" /></p>
		<p>State:<input type="text" name="State" /></p>
		<p>Phone:<input type="tel" name="Phone" /></p>
		<p>Email:<input type="email" name="Email" /></p>
		<p>Password:<input type="password" name="Password" /></p>
		<input type="hidden" name="return_url" value="'.$current_url.'" />
		<input type="submit" value="Create Account" />


		</form>
	<?php
	}
	else{
		header('Location:store.php');
	}
	?>
	</body>
	
</html>