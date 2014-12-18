<#-- dump.ftl
  --
  -- Generates tree representations of data model items.
  --
  -- Usage:
  -- <#import "dump.ftl" as dumper>
  --
  -- <#assign foo = something.in["your"].data[0].model />
  --
  -- <@dumper.dump foo />
  --
  -- When used within html pages you've to use <pre>-tags to get the wanted
  -- result:
  -- <pre>
  -- <@dumper.dump foo />
  -- <pre>
  -->
 
<#-- The black_list contains bad hash keys. Any hash key which matches a 
  -- black_list entry is prevented from being displayed.
  -->
<#assign black_list = ["class", "request", "getreader", "getinputstream", "writer"] />
 
 
<#-- 
  -- The main macro.
  -->
  
<#macro dump key data>
	<#if data?is_enumerable>
		<p><b>${key}</b>
		<@printList data,[] />
	<#elseif data?is_hash_ex>
		<p><b>${key}</b>
		<@printHashEx data,[] />
	<#else>
		<p><@printItem data?if_exists,[], key, false />
	</#if>
</#macro>
 
<#-- private helper macros. it's not recommended to use these macros from 
  -- outside the macro library.
  -->
 
<#macro printList list has_next_array>
	<#local counter=0 />
	<#list list as item>
		<#list has_next_array+[true] as has_next><#if !has_next>    <#else>  | </#if></#list>
		<#list has_next_array as has_next><#if !has_next>    <#else>  | </#if></#list><#t>
		<#t><@printItem item?if_exists,has_next_array+[item_has_next], counter, false />
		<#local counter = counter + 1/>
	</#list>
</#macro>
 
<#macro printHashEx hash has_next_array>
	= <code>${(hash.data!(hash.toString()))!"No String Representation"}</code>
	<ul>Callable methods:
		<ul>
		<#list hash?keys as key>
			<#list has_next_array+[true] as has_next><#if !has_next>    <#else>  | </#if></#list>
			<#list has_next_array as has_next><#if !has_next>    <#else>  | </#if></#list><#t>
			${key}
		</#list>
		</ul>
	</ul>
</#macro>
 
<#macro printHashExFull hash has_next_array>
	<#list hash?keys as key>
		<#list has_next_array+[true] as has_next><#if !has_next>    <#else>  | </#if></#list>
		<#list has_next_array as has_next><#if !has_next>    <#else>  | </#if></#list><#t>
		<#t><@printItem hash[key]?if_exists,has_next_array+[key_has_next], key, true />
	</#list>
</#macro>
 
<#macro printItem item has_next_array key full>
	<#if item?is_method>
		+- <b>${key}</b> = ?? (method)
	<#elseif item?is_enumerable>
		+- <b>${key}</b>
  		<@printList item, has_next_array /><#t>
	<#elseif item?is_hash_ex && omit(key?string)><#-- omit bean-wrapped java.lang.Class objects -->
		+- <b>${key}</b> (omitted)
	<#elseif item?is_hash_ex>
		+- <b>${key}</b>
  		<#if full>
  			<@printHashExFull item, has_next_array /><#t>
		<#else>
			<@printHashEx item, has_next_array /><#t>
		</#if>
	<#elseif item?is_number>
		+- <b>${key}</b> = <code>${item}</code>
	<#elseif item?is_string>
		+- <b>${key}</b> = <code>"${item}"</code>
	<#elseif item?is_boolean>
		+- <b>${key}</b> = <code>${item?string}</code>
	<#elseif item?is_date>
		+- <b>${key}</b> = <code>${item?string("yyyy-MM-dd HH:mm:ss zzzz")}</code>
	<#elseif item?is_transform>
		+- <b>${key}</b> = ?? (transform)
	<#elseif item?is_macro>
		+- <b>${key}</b> = ?? (macro)
	<#elseif item?is_hash>
		+- <b>${key}</b> = ?? (hash)
	<#elseif item?is_node>
		+- <b>${key}</b> = ?? (node)
	</#if>
</#macro>
 
<#function omit key>
    <#local what = key?lower_case>
    <#list black_list as item>
        <#if what?index_of(item) gte 0>
            <#return true>
        </#if>
    </#list>
    <#return false>
</#function>
 
<h1>All Liferay Freemarker Portlet Variables</h1>
<#list .data_model?keys as var>
	<#if var?index_of("writer") lt 0>
    	<@dump var,.data_model[var] />
	</#if>
</#list>
