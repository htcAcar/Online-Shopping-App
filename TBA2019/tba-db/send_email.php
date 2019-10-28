<?php
// the message
$to = "asma.aiouez@gmail.com";
$subject = "Shipping Confirmation Email";

// use wordwrap() if lines are longer than 70 characters
$msg = "Dear Customer,<br/><br>We're happy to let you know that the items you purchased are on their way to you:)<br>Thank you for your time and for shopping with us ^^.<br>TBA Ltd. ";

$headers = "From: TBA Ltd. <tba.contact.system@gmail.com>\r\n";
$headers .= "Reply-To: tba.contact.system@gmail.com\r\n";
$headers .= "Content-type: text/html\r\n";

// send email
if(mail($to,$subject,$msg, $headers)){
	var_dump("expression");
}else{
	var_dump("expression2");
}
?>