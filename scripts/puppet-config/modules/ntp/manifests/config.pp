class ntp::config() {
  $ntp_servers = hiera("ntp_servers")
  file{"/etc/ntp.conf":
    content => template("ntp/ntp.conf")
  }
}

