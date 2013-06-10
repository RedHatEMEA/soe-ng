<#if metaParameters?has_content>
	<#list metaParameters as metaParameter>
    ${metaParameter.key?lower_case?right_pad(15)} => [ <#list metaParameter.value as puppetType><#rt>
    		${puppetType.typeName}<#if puppetType_has_next>, </#if><#t>
    	</#list> ]<#t>
	</#list>
</#if>
