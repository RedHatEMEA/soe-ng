class jbosseap::packages {
  $packages = [ 
      'jbossas-appclient', 
      'jbossas-bundles', 
      'jbossas-core', 
      'jbossas-hornetq-native', 
      'jbossas-jbossweb-native',
      'jbossas-modules-eap', 
      'jbossas-product-eap', 
      'jbossas-standalone', 
      'jbossas-welcome-content-eap',
  ]  

  package { $packages: ensure => 'latest' }
}
