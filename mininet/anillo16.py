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

def myNetwork():

    net = Mininet( topo=None,
                   build=False,
                   ipBase='10.0.0.0/8')

    info( '*** Adding controller\n' )
    c0=net.addController(name='c0',
                      controller=RemoteController,
                      ip='192.168.1.3',
                      protocol='tcp',
                      port=6633)

    info( '*** Add switches\n')
    s13 = net.addSwitch('s13', cls=OVSKernelSwitch)
    s10 = net.addSwitch('s10', cls=OVSKernelSwitch)
    s3 = net.addSwitch('s3', cls=OVSKernelSwitch)
    s6 = net.addSwitch('s6', cls=OVSKernelSwitch)
    s2 = net.addSwitch('s2', cls=OVSKernelSwitch)
    s1 = net.addSwitch('s1', cls=OVSKernelSwitch)
    s9 = net.addSwitch('s9', cls=OVSKernelSwitch)
    s16 = net.addSwitch('s16', cls=OVSKernelSwitch)
    s12 = net.addSwitch('s12', cls=OVSKernelSwitch)
    s15 = net.addSwitch('s15', cls=OVSKernelSwitch)
    s5 = net.addSwitch('s5', cls=OVSKernelSwitch)
    s11 = net.addSwitch('s11', cls=OVSKernelSwitch)
    s8 = net.addSwitch('s8', cls=OVSKernelSwitch)
    s7 = net.addSwitch('s7', cls=OVSKernelSwitch)
    s4 = net.addSwitch('s4', cls=OVSKernelSwitch)
    s14 = net.addSwitch('s14', cls=OVSKernelSwitch)

    info( '*** Add hosts\n')
    h11 = net.addHost('h11', cls=Host, ip='10.0.0.11', defaultRoute=None)
    h3 = net.addHost('h3', cls=Host, ip='10.0.0.3', defaultRoute=None)
    h10 = net.addHost('h10', cls=Host, ip='10.0.0.10', defaultRoute=None)
    h5 = net.addHost('h5', cls=Host, ip='10.0.0.5', defaultRoute=None)
    h4 = net.addHost('h4', cls=Host, ip='10.0.0.4', defaultRoute=None)
    h1 = net.addHost('h1', cls=Host, ip='10.0.0.1', defaultRoute=None)
    h12 = net.addHost('h12', cls=Host, ip='10.0.0.12', defaultRoute=None)
    h8 = net.addHost('h8', cls=Host, ip='10.0.0.8', defaultRoute=None)
    h2 = net.addHost('h2', cls=Host, ip='10.0.0.2', defaultRoute=None)
    h6 = net.addHost('h6', cls=Host, ip='10.0.0.6', defaultRoute=None)
    h7 = net.addHost('h7', cls=Host, ip='10.0.0.7', defaultRoute=None)
    h9 = net.addHost('h9', cls=Host, ip='10.0.0.9', defaultRoute=None)

    info( '*** Add links\n')
    bw = {'bw':1000}
    net.addLink(s1, s3, cls=TCLink , **bw)
    net.addLink(s3, s4, cls=TCLink , **bw)
    net.addLink(s4, s2, cls=TCLink , **bw)
    net.addLink(s2, s1, cls=TCLink , **bw)
    net.addLink(s1, s16, cls=TCLink , **bw)
    net.addLink(s16, s15, cls=TCLink , **bw)
    net.addLink(s15, s14, cls=TCLink , **bw)
    net.addLink(s14, s1, cls=TCLink , **bw)
    net.addLink(s3, s13, cls=TCLink , **bw)
    net.addLink(s13, s12, cls=TCLink , **bw)
    net.addLink(s12, s11, cls=TCLink , **bw)
    net.addLink(s11, s3, cls=TCLink , **bw)
    net.addLink(s8, s10, cls=TCLink , **bw)
    net.addLink(s4, s8, cls=TCLink , **bw)
    net.addLink(s10, s9, cls=TCLink , **bw)
    net.addLink(s9, s4, cls=TCLink , **bw)
    net.addLink(s2, s7, cls=TCLink , **bw)
    net.addLink(s7, s6, cls=TCLink , **bw)
    net.addLink(s6, s5, cls=TCLink , **bw)
    net.addLink(s5, s2, cls=TCLink , **bw)
    net.addLink(h2, s15, cls=TCLink , **bw)
    net.addLink(h1, s14, cls=TCLink , **bw)
    net.addLink(h3, s16, cls=TCLink , **bw)
    net.addLink(h4, s5, cls=TCLink , **bw)
    net.addLink(h5, s6, cls=TCLink , **bw)
    net.addLink(h6, s7, cls=TCLink , **bw)
    net.addLink(h7, s9, cls=TCLink , **bw)
    net.addLink(h8, s10, cls=TCLink , **bw)
    net.addLink(h9, s8, cls=TCLink , **bw)
    net.addLink(h10, s11, cls=TCLink , **bw)
    net.addLink(h11, s12, cls=TCLink , **bw)
    net.addLink(h12, s13, cls=TCLink , **bw)

    info( '*** Starting network\n')
    net.build()
    info( '*** Starting controllers\n')
    for controller in net.controllers:
        controller.start()

    info( '*** Starting switches\n')
    net.get('s13').start([c0])
    net.get('s10').start([c0])
    net.get('s3').start([c0])
    net.get('s6').start([c0])
    net.get('s2').start([c0])
    net.get('s1').start([c0])
    net.get('s9').start([c0])
    net.get('s16').start([c0])
    net.get('s12').start([c0])
    net.get('s15').start([c0])
    net.get('s5').start([c0])
    net.get('s11').start([c0])
    net.get('s8').start([c0])
    net.get('s7').start([c0])
    net.get('s4').start([c0])
    net.get('s14').start([c0])

    info( '*** Post configure switches and hosts\n')

    CLI(net)
    net.stop()

if __name__ == '__main__':
    setLogLevel( 'info' )
    myNetwork()