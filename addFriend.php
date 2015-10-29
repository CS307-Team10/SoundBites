<?php
    $con=mysqli_connect("localhost","nganessi_tester","webhost94","nganessi_logintutorial");
      
    $username = $_POST["username"];
    $currentUser =  $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM User WHERE username = ?");
    mysqli_stmt_bind_param($statement, "s", $username);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $name, $age, $username, $password);
    
    $user = array();
    
    while(mysqli_stmt_fetch($statement)){
        $user["name"] = $name;
        $user["age"] = $age;
        $user["username"] = $username;
        $user["password"] = $password;
    }
    
    echo json_encode($user);

    $statement1 = "INSERT INTO `$currentUser` (friendName)
    VALUES('$username')";

    if (mysqli_query($con, $statement1)) {
        echo "New record created successfully";
    } else {
        echo "Error: " . $statement1 . "<br>" . mysqli_error($con);
    }

    mysqli_close($con);
?>