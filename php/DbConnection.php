<?php
   $con=mysqli_connect("http://167.99.76.96:3306","arm","L2#j6^%hcyb_?ABAB","auto_recharge_system");
   $sql="CREATE TABLE IF NOT EXISTS user_info (user_id varchar(255),phone_no varchar(255),email varchar(255),shop_name varchar(255),mac_address varchar(255),serial_key varchar(255),active_date varchar(255),expaied_date varchar(255),package_name varchar(255),price varchar(255),client_name varchar(255),initial_password varchar(255),shop_address varchar(255),package_validity varchar(255),role varchar(255));";
   if (mysqli_query($con,$sql)) {
      echo "Table have been created successfully";
   }else{
   	echo mysqli_error($con);
   }
?>