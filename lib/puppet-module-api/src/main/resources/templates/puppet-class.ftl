###################################################################
# Class ${name} is auto-generated. Do not edit manually.
###################################################################

class ${name} <#if params?has_content>(
<#assign keys = params?keys>
<#list keys as key>
  ${r"$"}${key} = ${r"$"}${params[key]},
</#list>
) </#if>{

<#if dependencies?has_content>
	<#list dependencies as dependency>
  include ${dependency}
	</#list>
</#if>

<#list typeOutputs as typeOutput>
${typeOutput}

</#list>
}