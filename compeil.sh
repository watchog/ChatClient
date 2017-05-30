#!/bin/bash
reset
s=`javac -d . UserInfo.java`
if [ -z $s ]
then
	echo "UserInfo.java 编译成功"
else
	echo "$s"
fi



echo `javac -d . Message.java`
if [ -z $s ]
then
	echo "Message.java 编译成功"
else
	echo "$s"
fi
echo `javac -d . MessageLinkedList.java`
if [ -z $s ]
then
	echo "MessageLinkedList.java 编译成功"
else
	echo "$s"
fi

echo `javac -d . GUIManager.java`
if [ -z $s ]
then
	echo "GUIManager.java 编译成功"
else
	echo "$s"
fi
echo `javac -d . ChatGUI.java`
if [ -z $s ]
then
	echo "ChatGUI.java 编译成功"
else
	echo "$s"
fi

echo `javac -d . ReceivePool.java`
if [ -z $s ]
then
	echo "ReceivePool.java 编译成功"
else
	echo "$s"
fi

echo `javac -d . SendPool.java`
if [ -z $s ]
then
	echo "SendPool.java 编译成功"
else
	echo "$s"
fi

echo `javac ChatClient.java`
if [ -z $s ]
then
	echo "ChatClient.java 编译成功"
else
	echo "$s"
fi
