# Food Flip

# Database Setup

1. Install XAMPP.
2. In XAMPP, start Apache and MySQL.
3. Log in to phpMyAdmin by navigating to http://localhost/phpmyadmin/ in your browser.
4. Create a database in phpMyAdmin named "ffdb".
5. Create a table in ffdb named *food_entries* with seven columns:

	* Building (varchar(255))
	* Location (varchar(255))
	* FoodCategory (varchar(10))
	* FoodType (varchar(255))
	* FoodDescription (varchar(255))
	* id (int(255))
	* price  (varchar(255))
	* Votes (int - default 0)
	* syncsts (tinyint - default value 0) - this will possibly be used for syncing new entries.
	
6. Create another table in ffdb named *users* with 4 columns:

	* user_id (varchar(255))
	* submissions(int()- default 0)
	* comments (int()- default 0)
	* karma (int()- default 0)

7. Create another table in ffdb named *votes* with 2 columns:
	* id (int(255))
	* user_id (varchar(255))

8. Create another table in ffdb named *food_entry_comment* with 2 columns:
	* id (int(255))
	* comment (varchar(1000))

9. In **FFDBController** change the *IP_ADDRESS* IP address to the address of your local machine.
10. Create a folder in xampp/htdocs named *foodflip* and copy the contents of the php folder to xampp/htdocs/foodflip.
11. In XAMPP click on *Shell*.
12. In the bash that opens up, type *mysql -u root -p*. When it asks for a password, just press ENTER.
13. You should now see 'mysql>' on the left. Type the following, pressing ENTER after each input:

	* use mysql;
	* UPDATE mysql.user
	* SET Password=PASSWORD("admin")
	* WHERE User="root";
	* FLUSH PRIVILEGES;
	* exit
	
14. Back in XAMPP click on *Config* for *Apache*, then click *phpMyAdmin (config.inc.php)*.
15. Find the line that says "$cfg['Servers'][$i]['password'] = '';" and change it to "$cfg['Servers'][$i]['password'] = 'admin';".
	

