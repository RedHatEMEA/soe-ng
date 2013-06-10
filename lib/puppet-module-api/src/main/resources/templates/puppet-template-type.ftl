  file { '${destination}':
    ensure          => file,
    owner           => '${owner}',
    content         => template('${name}'),
    mode            => '${mode}',
<#if metaParams?has_content>
${metaParams}
</#if>
  }