<?php
header('Content-Type: text/event-stream');
header('Cache-Control: no-cache');
session_start();

echo "retry: 100\n";

$dbhost = "localhost:3306";
$dbuser = "root";
$dbpass = "";
$conn = mysqli_connect($dbhost, $dbuser, $dbpass);
if(! $conn ) {
    die('Could not connect: ' . mysql_error());
}
mysqli_select_db($conn, "data");

if (!isset($_GET['query'])) {
    die();
}
$result = mysqli_query($conn, "SELECT `X`, `Y` FROM `current` WHERE `fetched`!=TRUE;");

if (mysqli_num_rows($result) > 0) {
    $result = mysqli_fetch_all($result, MYSQLI_ASSOC);
    foreach ($result as $row) {
        echo "data";
    }
}


echo "data: {$data}\n\n";
flush();
?>
