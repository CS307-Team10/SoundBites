<?php
    $con=mysqli_connect("localhost","nganessi_tester","webhost94","nganessi_logintutorial");
      
    $userName = $_POST["userName"];
    $friendName =  $_POST["friendName"];
    $highPitch = $_POST["highPitch"];
    $lowPitch = $_POST["lowPitch"];
    $speedUp = $_POST["speedUp"];
    $slowDown = $_POST["slowDown"];

    $statement = "UPDATE $userName SET fileName = '$userName$friendName.3gp' where friendName = '$friendName'";

    if (mysqli_query($con, $statement)) {
        echo "New record created successfully";
    } else {
        echo "Error: " . $statement . "<br>" . mysqli_error($con);
    }

    mysqli_close($con);
?>