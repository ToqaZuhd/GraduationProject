<?php 

$id = "";
if(isset($_GET['id'])){
	$id = $_GET['id'];
}


	$server_name = "localhost";
	$username = "root";
	$password = "";
	$dbname = "graduation_project";
	$data=array();
	
	$conn = new mysqli ($server_name, $username, $password, $dbname);

	if($conn->connect_error){
		die("Connection failed: " . $conn->connect_error);
	}

	
	$sql = "SELECT  car_number , car_type , rentDate ,endRentalRequest , Name, phoneNum FROM car c ,rentalrequest r, trip t , userdata u WHERE c.car_number = r.CarNumber AND t.TripID = r.TripID AND t.PassengerID = u.userId  AND c.providerID = " . $id ;

	$result=mysqli_query($conn,$sql);
	if($result)
	{
		while($row=mysqli_fetch_array($result))
		{
			$data[]=$row;
			

		}
		
		print(json_encode($data));
		

	}


	
	
	$conn->close();


 ?>






 




