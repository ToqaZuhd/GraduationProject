<?php


if($_SERVER['REQUEST_METHOD'] == 'POST') {
		$ID = isset($_POST['ID']) ? $_POST['ID'] : "";
		$score = isset($_POST['score']) ? $_POST['score'] : "";
		

		$server_name = "localhost";
		$username = "root";
		$password = "";
		$dbname = "graduation_project";
		
		$conn = new mysqli($server_name, $username, $password, $dbname);
		if($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		}


		$conn->query("UPDATE `passenger` set `score` =" . $score . " + ( SELECT `score` WHERE `ID` = '" . $ID ."' ) WHERE `ID` = '" . $ID ."'") ;


		
	

		if ( $conn->affected_rows == 0) {
			echo "failed" ;
			
		} else {
			echo "score Updated Successfully";
		}

		$conn->close();

	}	

?>