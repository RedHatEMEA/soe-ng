  package { '${packageName}':
    ensure          => '${version}',
<#if metaParams?has_content>
${metaParams}
</#if>
  }