  file { '${targetPath}':
    ensure          => file,
    owner           => '${owner}',
    source          => 'puppet:///modules/${moduleName}/${filename}',
    mode            => '${mode}',
<#if metaParams?has_content>
${metaParams}
</#if>
  }