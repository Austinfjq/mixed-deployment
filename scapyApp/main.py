#!/usr/bin/env python
# -*- coding: UTF-8 -*-
from scapy.all import *
import myhttp,configs

def processStr(data):
    pattern = re.compile('^b\'(.*?)\'$', re.S)
    res = re.findall(pattern, str(data))
    final = re.split('\\\\r\\\\n', res[0])
    return final
def process(ip_filter):
    dpkt = sniff(filter="tcp", count=30)
    #wrpcap('demo.pcap', p, append=True)
    for p in dpkt:
        #if p['IP'].proto==6:
        #p["TCP"].payload.show()
        if p.haslayer(myhttp.HTTPRequest):
            print("******HTTP Request******")
            http_name = 'HTTP Request'
            http_data = p[myhttp.HTTPRequest].fields
            method=http_data['Method'].decode('utf-8')
            path=http_data['Path'].decode('utf-8')
            host=http_data['Host'].decode('utf-8')
            path =path[:path.rfind("?")]
            src_ip=p["IP"].src
            dst_ip=p["IP"].dst
            sport=p["TCP"].sport
            dport=p["TCP"].dport
            if ip_filter in dst_ip:
                print('匹配成功!')
            print(src_ip)
            print(dst_ip)
            #print(sport)
            #print(dport)
            print(method)
            print(host)
            print(path)
            try:
                t1=p['TCP'].options[2][1][0]
                t2=p['TCP'].options[2][1][1]
                if t1>=t2:
                    t3=t1-t2
                else:
                    t3=t2-t1
            except:
                t3=0
            print(t3)
        elif p.haslayer(myhttp.HTTPResponse):
            print("******HTTP Response******")
            http_name = 'HTTP Response'
            http_data = p[myhttp.HTTPResponse].fields
            headers = http_data['Headers'].decode('utf-8')
            codes=http_data['Status-Line'].decode('utf-8')
            es=codes.split(' ')[1]
            src_ip=p["IP"].src
            dst_ip=p["IP"].dst
            sport=p["TCP"].sport
            dport=p["TCP"].dport
            if ip_filter in src_ip:
                print('匹配成功!')
            print(src_ip)
            print(dst_ip)
            #print(sport)
            #print(dport)
            print(es)
            print(headers)
            try:
                t1=p['TCP'].options[2][1][0]
                t2=p['TCP'].options[2][1][1]
                if t1>=t2:
                    t3=t1-t2
                else:
                    t3=t2-t1
            except:
                t3 = 0
            print(t3)
if __name__ == '__main__':
    #wrpcap("demo.pcap", dpkt)
    #dpkt = sniff(offline='example_http.pcap')
    #p["TCP"].payload.show()
    ip_filter=configs.ip_filter.split('x')[0]
    i=10000
    while 1:
        process(ip_filter)
        #i-=1
