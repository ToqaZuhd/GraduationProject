<?php

$server_name = "localhost";
			$username = "root";
			$password = "";
			$dbname = "graduation_project";
			$data=array();
// Create connection
$conn = new mysqli($server_name,$username ,$password, $dbname);
 
 if($_SERVER['REQUEST_METHOD'] == 'POST')
 {
 $DefaultId = 0;
 
 $ImageData = $_POST['image'];

 $GetOldIdSQL ="SELECT userId FROM userdata";
 
 $Query = mysqli_query($conn,$GetOldIdSQL);
 
 while($row = mysqli_fetch_array($Query)){
 
 $DefaultId = $row['userId'];
 }
 
 $ImagePath = "images/$DefaultId.png";
 
 $ServerURL = "https://androidjsonblog.000webhostapp.com/$ImagePath";
 
 $InsertSQL = "insert into userdata (image) values ('$ServerURL')";
 
 if(mysqli_query($conn, $InsertSQL)){

 file_put_contents($ImagePath,base64_decode($ImageData));

 echo "Your Image Has Been Uploaded.";
 }
 
 mysqli_close($conn);
 }else{
 echo "Not Uploaded";
 }

?>