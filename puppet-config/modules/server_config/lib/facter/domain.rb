# Facter fact determining search domain from the FQDN
# depending on the /etc/facter/facts.d/ENC-CONFIG.txt
#
Facter.add("designated_domain") do
 confine :operatingsystem => :RedHat
 setcode do
   Facter::Util::Resolution.exec('grep designated_hostname /etc/facter/facts.d/ENC-CONFIG.txt | cut -d= -f2 | sed "s/^[[:alnum:]]\+\.//" ')
 end
end
