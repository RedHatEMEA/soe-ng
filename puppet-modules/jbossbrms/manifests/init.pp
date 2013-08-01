class jbossbrms (
  $bind_address = '127.0.0.1',
  $bind_address_mgmt = '127.0.0.1',
  $mcast_address= '230.0.0.4',
  $machine_ip_address = '127.0.0.1',  
) {

  class {'jbosseap':
    bind_address        => $bind_address,
    bind_address_mgmt   => $bind_address_mgmt,
    mcast_address       => $mcast_address,
    machine_ip_address   => $machine_ip_address
  }

  class {'jbossbrms::packages':
    require => Class['jbosseap::packages']
  }

  class {'jbossbrms::config':
    require => Class['jbossbrms::packages'],
    before  => Class['jbosseap:cli']
  }
}
