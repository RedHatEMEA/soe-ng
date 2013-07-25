class jbosseap (
  $bind_address = '127.0.0.1',
  $bind_address_mgmt = '127.0.0.1',
  $mcast_address= '230.0.0.4',
  $machine_ip_address = '127.0.0.1',  
) {

  require java

  class {'jbosseap::packages':
    before      => Class['jbosseap::service'],
  }

  class {'jbosseap::service':}

  class {'jbosseap::config':
    require          => Class['jbosseap::packages'],
    before           => Class['jbosseap::service'],
  }
  
  class {'jbosseap::cli':
    require          => Class['jbosseap::service'],
  }

  exec { 'Restart jbossas Service':
    command  => '/sbin/service jbossas restart',
    user     => 'root',
    group    => 'root',
    provider => 'posix',
    require  => Class['jbosseap::cli'],
  }
}
