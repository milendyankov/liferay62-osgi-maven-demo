
<#include "../init.ftl" />

<@liferay_portlet["actionURL"] name="addEntry" var="addEntryURL" />

<@aui["form"] action="${addEntryURL}" method="post" name="fm">

	<@aui["input"] 
		name="message" 
		type="text" />

	<@aui["button"] type="submit" value="Send" />
</@>

<ul>
<#assign messages = request.getAttribute("messages")>
<#list messages as message>
	<#assign inlineTemplate = message?interpret>
	<li><@inlineTemplate /></li>
</#list>  
</ul>
		


