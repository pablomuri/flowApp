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
    s4 = net.addSwitch('s4', cls=OVSKernelSwitch)
    s11 = net.addSwitch('s11', cls=OVSKernelSwitch)
    s8 = net.addSwitch('s8', cls=OVSKernelSwitch)
    s12 = net.addSwitch('s12', cls=OVSKernelSwitch)
    s5 = net.addSwitch('s5', cls=OVSKernelSwitch)
    s9 = net.addSwitch('s9', cls=OVSKernelSwitch)
    s2 = net.addSwitch('s2', cls=OVSKernelSwitch)
    s10 = net.addSwitch('s10', cls=OVSKernelSwitch)
    s1 = net.addSwitch('s1', cls=OVSKernelSwitch)
    s7 = net.addSwitch('s7', cls=OVSKernelSwitch)
    s6 = net.addSwitch('s6', cls=OVSKernelSwitch)
    s3 = net.addSwitch('s3', cls=OVSKernelSwitch)

    info( '*** Add hosts\n')
    h2 = net.addHost('h2', cls=Host, ip='10.0.0.2', defaultRoute=None)
    h6 = net.addHost('h6', cls=Host, ip='10.0.0.6', defaultRoute=None)
    h3 = net.addHost('h3', cls=Host, ip='10.0.0.3', defaultRoute=None)
    h4 = net.addHost('h4', cls=Host, ip='10.0.0.4', defaultRoute=None)
    h5 = net.addHost('h5', cls=Host, ip='10.0.0.5', defaultRoute=None)
    h1 = net.addHost('h1', cls=Host, ip='10.0.0.1', defaultRoute=None)

    info( '*** Add links\n')
    net.addLink(h1, s1)
    s1s3 = {'bw':1000}
    net.addLink(s1, s3, cls=TCLink , **s1s3)
    s3s6 = {'bw':1000}
    net.addLink(s3, s6, cls=TCLink , **s3s6)
    s6s10 = {'bw':1000}
    net.addLink(s6, s10, cls=TCLink , **s6s10)
    net.addLink(s10, h6)
    net.addLink(h2, s2)
    s2s4 = {'bw':1000}
    net.addLink(s2, s4, cls=TCLink , **s2s4)
    s4s5 = {'bw':1000}
    net.addLink(s4, s5, cls=TCLink , **s4s5)
    s5s11 = {'bw':1000}
    net.addLink(s5, s11, cls=TCLink , **s5s11)
    net.addLink(s11, h5)
    net.addLink(h3, s7)
    s7s8 = {'bw':1000}
    net.addLink(s7, s8, cls=TCLink , **s7s8)
    s8s9 = {'bw':1000}
    net.addLink(s8, s9, cls=TCLink , **s8s9)
    s9s12 = {'bw':1000}
    net.addLink(s9, s12, cls=TCLink , **s9s12)
    net.addLink(s12, h4)
    s8s4 = {'bw':1000}
    net.addLink(s8, s4, cls=TCLink , **s8s4)
    s9s5 = {'bw':1000}
    net.addLink(s9, s5, cls=TCLink , **s9s5)
    s5s6 = {'bw':1000}
    net.addLink(s5, s6, cls=TCLink , **s5s6)
    s4s3 = {'bw':1000}
    net.addLink(s4, s3, cls=TCLink , **s4s3)
    s1s2 = {'bw':1000}
    net.addLink(s1, s2, cls=TCLink , **s1s2)
    s2s7 = {'bw':1000}
    net.addLink(s2, s7, cls=TCLink , **s2s7)
    s10s11 = {'bw':1000}
    net.addLink(s10, s11, cls=TCLink , **s10s11)
    s11s12 = {'bw':1000}
    net.addLink(s11, s12, cls=TCLink , **s11s12)

    info( '*** Starting network\n')
    net.build()
    info( '*** Starting controllers\n')
    for controller in net.controllers:
        controller.start()

    info( '*** Starting switches\n')
    net.get('s4').start([c0])
    net.get('s11').start([c0])
    net.get('s8').start([c0])
    net.get('s12').start([c0])
    net.get('s5').start([c0])
    net.get('s9').start([c0])
    net.get('s2').start([c0])
    net.get('s10').start([c0])
    net.get('s1').start([c0])
    net.get('s7').start([c0])
    net.get('s6').start([c0])
    net.get('s3').start([c0])

    info( '*** Post configure switches and hosts\n')

    CLI(net)
    net.stop()

if __name__ == '__main__':
    setLogLevel( 'info' )
    myNetwork()

