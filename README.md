# ChatClient
如何运行
	1.确保系统安装了java，并且运行软件的机器之间可以互相通信
	2.进入客户端或服务端的源码目录,执行以下操作：
		1.touch localIpAddress(客户端)
		1.touch Account(服务端)
		第一步后打开localIpAddress输入本机的ip地址	
		2.sudo chmod u+x compeil.sh
		3../compeil.sh
		4.java ChatServer(服务端) java ChatClient(客户端)
	3.如果运行的是客户端，在登录界面上点击设置输入服务端ip地址
	
	
