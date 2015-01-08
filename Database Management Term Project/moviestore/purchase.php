<?php
session_start();

echo '<h1>Thank you for your purchase, you are now logged out</h1>';
unset($_SESSION['Login']);
session_destroy();
echo "<a href='store.php'>Back to the store</a><br>";
?>