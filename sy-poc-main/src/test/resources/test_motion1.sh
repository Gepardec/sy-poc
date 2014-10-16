#!/bin/bash

#./motion <ID> <Type>

#If nothing
if [ x$1 = x ]; then
	echo "X FAIL"
	exit 1;	
fi

#If only id
if [ x$2 = x ]; then
	echo "$1 FAIL"
	exit 1;
fi

#If type is not Mail
if [ "$2" != "mail" ]; then
	echo "$1 FAIL"
	exit 1
fi

#Everything ok
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<!--MOCK RESULT-->
<message name=\"feedback\" xmlns=\"http://gepardec.com/sy_poc/xml/message_response_1_0\">
	<service name=\"mail\">
		<action name=\"activate\">
			<data>
				<order_list>$1</order_list>
				<status>OK</status>
				<error_timestamp>OK</error_timestamp>
				<error_desc>Success</error_desc>
				<country>AT</country>
				<nofsmartcards>1</nofsmartcards>
				<priority>EMM</priority>
				<product_id>00000106</product_id>
				<subscription_end>201406302359</subscription_end>
				<subscription_start>201406010000</subscription_start>
				<!-- smartcard_list -->
				<smartcard>01359116803</smartcard>
				<!-- /smartcard_list -->
			</data>
		</action>
	</service>
</message>";
exit 0
