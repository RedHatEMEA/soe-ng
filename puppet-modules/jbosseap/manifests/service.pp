class jbosseap::service {

  service { 'jbossas':
    ensure  => true ? {
      true    => running,
      default => undef
    },
    enable  => true,
  }
}
