<?php


	$coordinate = "";
	
	if(isset($_GET['coordinate'])){
		$coordinate = $_GET['coordinate'];
	}
	
	
		$server_name = "localhost";
		$username = "root";
		$password = "";
		$dbname = "graduation_project";
		$response=array();
		
		$conn = new mysqli($server_name, $username, $password, $dbname);
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		} 
		
		$sql2="DELETE FROM `crisisdata` WHERE `enteredTime` < CURDATE()";
        $conn->query($sql2);
		
		$sql = "select * from crisisdata where coordinate = '" . $coordinate . "'";
		
		
		
		
		
		$result=mysqli_query($conn,$sql);
	    if($result)
	    {
		   while($row=mysqli_fetch_array($result))
		   {
			   
			$data[]=$row;
			
           }
		
		print(json_encode($data));
		

	    }
					
		
		
					
		echo json_encode($response);
		
		$conn->close();
		
	
	
	




?>