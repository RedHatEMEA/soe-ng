class jbossbrms::packages {
  $packages = [ 
      'jboss-brms-rpm', 
  ]  

  package { $packages: ensure => 'latest' }
}
