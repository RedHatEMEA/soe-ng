#Facter fact that defines the shortname of the server based on the designated_hostname
#in /etc/facter/facts.d/ENC-CONFIG.txt
Facter.add('designated_shortname') do
  confine :operatingsystem => :RedHat
  setcode do
    Facter::Util::Resolution.exec('grep designated_hostname /etc/facter/facts.d/ENC-CONFIG.txt | cut -d= -f2 | cut -d. -f1 ')
  end
end
