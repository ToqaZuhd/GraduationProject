<?php

	
	if($_SERVER['REQUEST_METHOD'] == "POST"){
		
		$email = isset($_POST['email']) ? $_POST['email'] : "";
		$Id= isset($_POST['id']) ? $_POST['id'] : "";
		$Name = isset($_POST['name']) ? $_POST['name'] : "";       
		$passwordUser = isset($_POST['pass']) ? $_POST['pass'] : "";
    $phone = isset($_POST['phone']) ? $_POST['phone'] : "";
		
		$server_name = "localhost";
		$username = "root";
		$password = "";
		$dbname = "graduation_project";

		$conn = new mysqli($server_name, $username, $password, $dbname);
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		} 
		
		
	
		$sql = "update userdata set Name='" . $Name . "' where userId='" .$Id. "'";
		$sql2 = "update userdata set password='" . $passwordUser . "' where userId='" .$Id. "'";
		$sql3 = "update userdata set phoneNum='" . $phone . "' where userId='" .$Id. "'";
		$sql4 = "update userdata set email='" . $email . "' where userId='" .$Id. "'";

        if(!empty($Name)){
          if ($conn->query($sql) === TRUE) {
		     $response['error'] = false;
		     $response['message'] = "تم تغيير الاسم بنجاح";
		  }
		}
		
		else if(!empty($passwordUser)){
           if ($conn->query($sql2) === TRUE) {
		      $response['error'] = false;
		      $response['message'] = "تم تغيير كلمة السر بنجاح";
		   }
		}
		
		else if(!empty($phone)){

         if ($conn->query($sql3) === TRUE) {
	         $response['error'] = false;
		     $response['message'] = "تم تغيير رقم الهاتف بنجاح";
		 }
		}
		else if(!empty($email)){

          if ($conn->query($sql4) === TRUE) {		
             $response['error'] = false;
		     $response['message'] = "تم تغير الإيميل بنجاح";
         }
		}
	
	
		echo json_encode($response);

		$conn->close();
	
	}


?>