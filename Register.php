<?php
    $con=mysqli_connect("localhost","nganessi_tester","webhost94","nganessi_logintutorial");
    
    $name = $_POST["name"];
    $age = $_POST["age"];
    $username = $_POST["username"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "INSERT INTO `User` (name, age, username, password) VALUES (?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "siss", $name, $age, $username, $password);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_close($statement);
    
    $statement2 = "CREATE TABLE $username(
        id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        friendName VARCHAR(30) NOT NULL,
        fileName VARCHAR(30) NOT NULL,
        highPitch INT(6) NOT NULL,
        lowPitch INT(6) NOT NULL,
        speedUp INT(6) NOT NULL,
        slowDown INT(6) NOT NULL)";
    
    if($con->query($statement2) === TRUE){
        echo "Table successfully created!";
    } else {
        echo "Error creating table: " .$con->error;
    }

    mysqli_close($con);
?>