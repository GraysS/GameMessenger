<h1>IMyAcc</h1>

<form action="API/IMyAcc/registration.php" method="POST">
	Nick <input type="text" name="nick">	
	Password:<input type="Password" name="password">
	Firebase_token:<input type="text" name="firebase_token">
    <input type="text" value="registration.php" disabled="true">
	<input type="submit" value="Отправить">
</form>


<form action="API/IMyAcc/login.php" method="POST">
	Nick <input type="text" name="nick">	
	Password:<input type="Password" name="password">
	Firebase_token:<input type="text" name="firebase_token">
    <input type="text" value="login.php" disabled="true">
	<input type="submit" value="Отправить">
</form>

<h1>IMyAcc</h1>

<h1>IUsers</h1>

<form action="API/IUsers/getListUsers.php" method="POST">
	id:<input type="number" name="id">
	friends_id:<input type="number" name="friends_id">
    <input type="text" value="getListUsers.php" disabled="true">
	<input type="submit" value="Отправить">
</form>



<form action="API/IUsers/isNotificationUser.php"  method="POST">
	my_id:<input type="number" name="my_id">
	friends_id:<input type="number" name="friends_id">
	<input type="text"  value="isNotificationUser.php" disabled="true">
	<input type="submit" value="Отправить">
</form>


<form action="API/IUsers/AddNotificationUser.php"  method="POST">
	my_id:<input type="number" name="my_id">
	friends_id:<input type="number" name="friends_id">
	<input type="text"  value="AddNotificationUser.php" disabled="true">
	<input type="submit" value="Отправить">
</form>

<h1>IUsers</h1>

<h1>INotification</h1>



<form action="API/INotification/getNotification.php"  method="POST">
	my_id:<input type="number" name="my_id">
	<input type="text"  value="getNotification.php" disabled="true">
	<input type="submit" value="Отправить">
</form>

<h1>INotification</h1>

<h1>IFriends</h1>

<form action="API/IFriends/AddFriendsNotification.php"  method="POST">
	my_id:<input type="number" name="my_id">
	friends_id:<input type="number" name="friends_id">
	<input type="text"  value="AddFriendsNotification.php" disabled="true">
	<input type="submit" value="Отправить">
</form>

<form action="API/IFriends/getFriends.php"  method="POST">
	my_id:<input type="number" name="my_id">
	<input type="text"  value="getFriends.php" disabled="true">
	<input type="submit" value="Отправить">
</form>


<form action="API/IFriends/deleteFriends.php"  method="POST">
	my_id:<input type="number" name="my_id">
	friends_id:<input type="number" name="friends_id">
	<input type="text"  value="deleteFriends.php" disabled="true">
	<input type="submit" value="Отправить">
</form>

<h1>IFriends</h1>

<h1>IMessageFriends</h1>

<form action="API/IMessageFriends/AddMessageFriends.php"  method="POST">
	my_id:<input type="number" name="my_id">
	friends_id:<input type="number" name="friends_id">
	message :<input type="text" name="message">	
	dates:<input type="number" name="dates">
	<input type="text"  value="AddMessageFriends.php" disabled="true">
	<input type="submit" value="Отправить">
</form>

<form action="API/IMessageFriends/getMessageFriends.php"  method="POST">
	my_id:<input type="number" name="my_id">
	friends_id:<input type="number" name="friends_id">
	<input type="text"  value="getMessageFriends.php" disabled="true">
	<input type="submit" value="Отправить">
</form>
<!-- 
<form action="API/IMessageFriends/getThreadMessageFriends.php"  method="POST">
	my_id:<input type="number" name="my_id">
	friends_id:<input type="number" name="friends_id">
	<input type="text"  value="getThreadMessageFriends.php" disabled="true">
	<input type="submit" value="Отправить">
</form> -->


<form action="API/IMessageFriends/deleteMessageFriends.php"  method="POST">
	my_id:<input type="number" name="my_id">
	friends_id:<input type="number" name="friends_id">
	id:<input type="number" name="id">
	<input type="text"  value="deleteMessageFriends.php" disabled="true">
	<input type="submit" value="Отправить">
</form>

<h1>IMessageFriends</h1>







