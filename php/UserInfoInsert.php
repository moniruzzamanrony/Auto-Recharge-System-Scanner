<?php
   $con=mysqli_connect("localhost:3306","root","","auto_recharge_system");

   if (mysqli_connect_errno($con)) {
      echo "Failed to connect to MySQL: " . mysqli_connect_error();
   }

   $user_id = $_GET['user_id'];
   $phone_no = $_GET['phone_no'];
   $email = $_GET['email'];
   $shop_name = $_GET['shop_name'];
   $mac_address = $_GET['mac_address'];
   $serial_key = $_GET['serial_key'];
   $active_date = $_GET['active_date'];
   $expaied_date = $_GET['expaied_date'];
   $package_name = $_GET['package_name'];
   $price = $_GET['price'];
   $client_name = $_GET['client_name'];
   $initial_password = $_GET['initial_password'];
   $shop_address = $_GET['shop_address'];
   $package_validity = $_GET['package_validity'];
   $role = $_GET['role'];

   $query = INSERT INTO `user_info`(`user_id`, `phone_no`, `email`, `shop_name`, `mac_address`, `serial_key`, `active_date`, `expaied_date`, `package_name`, `price`, `client_name`, `initial_password`, `shop_address`, `package_validity`, `role`) VALUES ('$user_id','$phone_no','$email','$shop_name','$mac_address','$serial_key','$active_date','$expaied_date','$package_name','$price','$client_name','$initial_password','$shop_address','$package_validity','$role')";


   $result = mysqli_query($con,$query);
   // $row = mysqli_fetch_array($result);
   // $data = $row[0];

   // if($data){
   //    echo $data;
   // }
   mysqli_close($con);
?>