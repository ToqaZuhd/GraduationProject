<?php

	
			$server_name = "localhost";
			$username = "root";
			$password = "";
			$dbname = "graduation_project";
			$data=array();

	$con=mysqli_connect($server_name, $username, $password, $dbname) or die('Unable to connect');


   
	$sql="SELECT userdata.name as name,userdata.image as image, review.date as date, review.review as review,review.rate as rate FROM userdata INNER JOIN review ON userdata.userId=review.userId";


	$result=mysqli_query($con,$sql);
	if($result)
	{
		while($row=mysqli_fetch_array($result))
		{
			$data[]=$row;
			

		}
		
		print(json_encode($data));
		

	}

	mysqli_close($con);

	?>