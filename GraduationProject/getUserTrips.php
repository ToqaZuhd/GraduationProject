<?php 


$PassengerID = "";
if(isset($_GET['PassengerID'])){
	$PassengerID = $_GET['PassengerID'];
}


	$server_name = "localhost";
	$username = "root";
	$password = "";
	$dbname = "graduation_project";
	
	$conn = new mysqli ($server_name, $username, $password, $dbname);

	if($conn->connect_error){
		die("Connection failed: " . $conn->connect_error);
	}

	$sql = "select * from trip where PassengerID = '".$PassengerID . "'" ;

	$result = $conn->query($sql);
	$data = "";
	

	if ($result->num_rows >0) {
		while($row = $result->fetch_assoc()){
			$data.= $row["TripID"]. " , " . $row["TicketID"] ." , " . $row["NumOfBags"] ." , " . $row["TripDate"] ." , " . $row["rewardPoints"] ."/ " ;
		}
		
		echo $data ;
	} else {
		echo "no result";
	}

	$conn->close();


 ?>
