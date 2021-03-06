#!/usr/bin/python

from mininet.net import Mininet
from mininet.node import Controller, RemoteController, OVSController
from mininet.node import CPULimitedHost, Host, Node
from mininet.node import OVSKernelSwitch, UserSwitch
from mininet.node import IVSSwitch
from mininet.cli import CLI
from mininet.log import setLogLevel, info
from mininet.link import TCLink, Intf
from subprocess import call
from random import choice
import time

net = Mininet( topo=None,build=False,ipBase='10.0.0.0/8')

def myNetwork():

    

    info( '*** Adding controller\n' )
    c0=net.addController(name='c0',
                      controller=RemoteController,
                      ip='192.168.1.3',
                      protocol='tcp',
                      port=6633)

    info( '*** Add switches\n')
    s39 = net.addSwitch('s39', cls=OVSKernelSwitch)
    s41 = net.addSwitch('s41', cls=OVSKernelSwitch)
    s42 = net.addSwitch('s42', cls=OVSKernelSwitch)
    s6 = net.addSwitch('s6', cls=OVSKernelSwitch)
    s4 = net.addSwitch('s4', cls=OVSKernelSwitch)
    s34 = net.addSwitch('s34', cls=OVSKernelSwitch)
    s11 = net.addSwitch('s11', cls=OVSKernelSwitch)
    s35 = net.addSwitch('s35', cls=OVSKernelSwitch)
    s13 = net.addSwitch('s13', cls=OVSKernelSwitch)
    s15 = net.addSwitch('s15', cls=OVSKernelSwitch)
    s16 = net.addSwitch('s16', cls=OVSKernelSwitch)
    s20 = net.addSwitch('s20', cls=OVSKernelSwitch)
    s14 = net.addSwitch('s14', cls=OVSKernelSwitch)
    s10 = net.addSwitch('s10', cls=OVSKernelSwitch)
    s1 = net.addSwitch('s1', cls=OVSKernelSwitch)
    s37 = net.addSwitch('s37', cls=OVSKernelSwitch)
    s43 = net.addSwitch('s43', cls=OVSKernelSwitch)
    s40 = net.addSwitch('s40', cls=OVSKernelSwitch)
    s28 = net.addSwitch('s28', cls=OVSKernelSwitch)
    s22 = net.addSwitch('s22', cls=OVSKernelSwitch)
    s9 = net.addSwitch('s9', cls=OVSKernelSwitch)
    s26 = net.addSwitch('s26', cls=OVSKernelSwitch)
    s25 = net.addSwitch('s25', cls=OVSKernelSwitch)
    s36 = net.addSwitch('s36', cls=OVSKernelSwitch)
    s30 = net.addSwitch('s30', cls=OVSKernelSwitch)
    s46 = net.addSwitch('s46', cls=OVSKernelSwitch)
    s23 = net.addSwitch('s23', cls=OVSKernelSwitch)
    s29 = net.addSwitch('s29', cls=OVSKernelSwitch)
    s32 = net.addSwitch('s32', cls=OVSKernelSwitch)
    s18 = net.addSwitch('s18', cls=OVSKernelSwitch)
    s19 = net.addSwitch('s19', cls=OVSKernelSwitch)
    s44 = net.addSwitch('s44', cls=OVSKernelSwitch)
    s38 = net.addSwitch('s38', cls=OVSKernelSwitch)
    s7 = net.addSwitch('s7', cls=OVSKernelSwitch)
    s5 = net.addSwitch('s5', cls=OVSKernelSwitch)
    s33 = net.addSwitch('s33', cls=OVSKernelSwitch)
    s45 = net.addSwitch('s45', cls=OVSKernelSwitch)
    s31 = net.addSwitch('s31', cls=OVSKernelSwitch)
    s27 = net.addSwitch('s27', cls=OVSKernelSwitch)
    s21 = net.addSwitch('s21', cls=OVSKernelSwitch)
    s2 = net.addSwitch('s2', cls=OVSKernelSwitch)
    s24 = net.addSwitch('s24', cls=OVSKernelSwitch)
    s17 = net.addSwitch('s17', cls=OVSKernelSwitch)
    s3 = net.addSwitch('s3', cls=OVSKernelSwitch)
    s12 = net.addSwitch('s12', cls=OVSKernelSwitch)
    s8 = net.addSwitch('s8', cls=OVSKernelSwitch)

    info( '*** Add hosts\n')
    h11 = net.addHost('h11', cls=Host, ip='10.0.0.11', defaultRoute=None)
    h8 = net.addHost('h8', cls=Host, ip='10.0.0.8', defaultRoute=None)
    h10 = net.addHost('h10', cls=Host, ip='10.0.0.10', defaultRoute=None)
    h5 = net.addHost('h5', cls=Host, ip='10.0.0.5', defaultRoute=None)
    h2 = net.addHost('h2', cls=Host, ip='10.0.0.2', defaultRoute=None)
    h7 = net.addHost('h7', cls=Host, ip='10.0.0.7', defaultRoute=None)
    h4 = net.addHost('h4', cls=Host, ip='10.0.0.4', defaultRoute=None)
    h3 = net.addHost('h3', cls=Host, ip='10.0.0.3', defaultRoute=None)
    h1 = net.addHost('h1', cls=Host, ip='10.0.0.1', defaultRoute=None)
    h9 = net.addHost('h9', cls=Host, ip='10.0.0.9', defaultRoute=None)
    h6 = net.addHost('h6', cls=Host, ip='10.0.0.6', defaultRoute=None)

    info( '*** Add links\n')
    net.addLink(s5, s1)
    net.addLink(s1, s2)
    net.addLink(s2, s3)
    net.addLink(s3, s4)
    net.addLink(s4, s6)
    net.addLink(s6, s7)
    net.addLink(s7, s5)
    net.addLink(s6, s8)
    net.addLink(s8, s9)
    net.addLink(s9, s10)
    net.addLink(s7, s13)
    net.addLink(s13, s12)
    net.addLink(s12, s14)
    net.addLink(s14, s11)
    net.addLink(s14, s10)
    net.addLink(s11, s15)
    net.addLink(s7, s16)
    net.addLink(s16, s17)
    net.addLink(s18, s19)
    net.addLink(s18, s20)
    net.addLink(s17, s18)
    net.addLink(s13, s21)
    net.addLink(s21, s19)
    net.addLink(s21, s22)
    net.addLink(s22, s11)
    net.addLink(s22, s23)
    net.addLink(s21, s26)
    net.addLink(s26, s25)
    net.addLink(s25, s24)
    net.addLink(s24, s15)
    net.addLink(s11, s25)
    net.addLink(s20, s27)
    net.addLink(s27, s28)
    net.addLink(s27, s30)
    net.addLink(s30, s28)
    net.addLink(s30, s33)
    net.addLink(s30, s34)
    net.addLink(s34, s33)
    net.addLink(s34, s35)
    net.addLink(s35, s28)
    net.addLink(s33, s32)
    net.addLink(s32, s31)
    net.addLink(s31, s21)
    net.addLink(s33, s36)
    net.addLink(s36, s37)
    net.addLink(s37, s34)
    net.addLink(s37, s38)
    net.addLink(s38, s39)
    net.addLink(s39, s40)
    net.addLink(s40, s35)
    net.addLink(s36, s41)
    net.addLink(s41, s25)
    net.addLink(s21, s41)
    net.addLink(s40, s42)
    net.addLink(s42, s43)
    net.addLink(s43, s44)
    net.addLink(s44, s25)
    net.addLink(s41, s42)
    net.addLink(s28, s45)
    net.addLink(s45, s46)
    net.addLink(s45, s29)
    net.addLink(h2, s40)
    net.addLink(h1, s1)
    net.addLink(h3, s15)
    net.addLink(h6, s45)
    net.addLink(h7, s33)
    net.addLink(h4, s21)
    net.addLink(h5, s17)
    net.addLink(h8, s8)
    net.addLink(h9, s12)
    net.addLink(h10, s4)
    net.addLink(s27, h11)

    info( '*** Starting network\n')
    net.build()
    info( '*** Starting controllers\n')
    for controller in net.controllers:
        controller.start()

    info( '*** Starting switches\n')
    net.get('s39').start([c0])
    net.get('s41').start([c0])
    net.get('s42').start([c0])
    net.get('s6').start([c0])
    net.get('s4').start([c0])
    net.get('s34').start([c0])
    net.get('s11').start([c0])
    net.get('s35').start([c0])
    net.get('s13').start([c0])
    net.get('s15').start([c0])
    net.get('s16').start([c0])
    net.get('s20').start([c0])
    net.get('s14').start([c0])
    net.get('s10').start([c0])
    net.get('s1').start([c0])
    net.get('s37').start([c0])
    net.get('s43').start([c0])
    net.get('s40').start([c0])
    net.get('s28').start([c0])
    net.get('s22').start([c0])
    net.get('s9').start([c0])
    net.get('s26').start([c0])
    net.get('s25').start([c0])
    net.get('s36').start([c0])
    net.get('s30').start([c0])
    net.get('s46').start([c0])
    net.get('s23').start([c0])
    net.get('s29').start([c0])
    net.get('s32').start([c0])
    net.get('s18').start([c0])
    net.get('s19').start([c0])
    net.get('s44').start([c0])
    net.get('s38').start([c0])
    net.get('s7').start([c0])
    net.get('s5').start([c0])
    net.get('s33').start([c0])
    net.get('s45').start([c0])
    net.get('s31').start([c0])
    net.get('s27').start([c0])
    net.get('s21').start([c0])
    net.get('s2').start([c0])
    net.get('s24').start([c0])
    net.get('s17').start([c0])
    net.get('s3').start([c0])
    net.get('s12').start([c0])
    net.get('s8').start([c0])

    info( '*** Post configure switches and hosts\n')

    net.pingAll()
    h1, h2, h3, h4, h5, h6, h7, h8, h9, h10, h11 = net.get('h1', 'h2', 'h3','h4','h5',
    	'h6', 'h7', 'h8', 'h9', 'h10', 'h11')

    while True:
    	hosts = [h1, h2, h3, h4, h5, h6, h7, h8, h9, h10]
    	for i in range(5):
    		a = choice(hosts)
    		hosts.pop(hosts.index(a))
    		b = choice(hosts)
    		hosts.pop(hosts.index(b))
    		iperfUDP(a, b)
    		time.sleep(5)
    	time.sleep(20)
    	killIperf(hosts)
    
    net.stop()

def iperfUDP(h1, h2):
	info('iperf desde ' + h1.IP() + ' hasta ' + h2.IP() + ' \n')
	h2.cmd('iperf -s -u&')
	h1.cmd('iperf -c ' + h2.IP() + ' -u -b 10m -d -t 20&')
	
def killIperf(hosts):
	for h in hosts:
		h.cmd('killall iperf&')

if __name__ == '__main__':
    setLogLevel( 'info' )
    myNetwork()

