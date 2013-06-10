  exec { '${comment}':
    command         => '${command}',
    user            => '${user}',
    group           => '${group}',
    <#if unless?has_content>
    unless          => '${unless}',
    </#if>
    provider        => 'posix',
<#if metaParams?has_content>
${metaParams}
</#if>
  }