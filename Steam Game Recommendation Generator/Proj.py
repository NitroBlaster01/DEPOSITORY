#!C:\Users\Nitrous\AppData\Local\Programs\Python\Python38-32\python.exe
import urllib.request
print ('Content-type: text/html\n')
import cgitb,cgi
cgitb.enable()
form=cgi.FieldStorage()
gn=form.getvalue('gamename')
#gn='Truck Simulator'
def find_nth(string, substring, n):
   if (n == 1):
       return string.find(substring)
   else:
       return string.find(substring, find_nth(string, substring, n - 1) + 1)
try:
    game=gn.replace(' ','+')
    with urllib.request.urlopen('https://store.steampowered.com/search/?term='+game+'&category1=998%2C994') as response:
        html = response.read()
    html=html.decode("utf-8")
    if html.find('No results were returned for that query.')>0:
        ho=open('hello3.html','r')
        be=ho.read()
        print(be)
    else:
        pos=html.find('''data-ds-tagids=''')
        tags=html[pos+17:html.find(''']" data-ds-crtrids=''')]
        if tags.find('data-ds-descids=')!=-1:
            tags=tags[:tags.find(''']"''')]
        else:
            tags=tags
        #print(tags)
        tags=tags.split(',')
        with urllib.request.urlopen('https://store.steampowered.com/search/?tags='+tags[0]+'%2C'+tags[1]+'%2C'+tags[2]+'&category1=998%2C994') as response:
                html = response.read()
        lmao=('https://store.steampowered.com/search/?tags='+tags[0]+'%2C'+tags[1]+'%2C'+tags[2]+'&category1=998%2C994')
        html=html.decode("utf-8")
        first=html.find('''<a href="https://store.steampowered.com/app/''')
        second= find_nth(html,'''<a href="https://store.steampowered.com/app/''',2)
        third= find_nth(html,'''<a href="https://store.steampowered.com/app/''',3)

        x=html[third+9:third+150]
        y=(x.find('''?snr='''))
        third=html[third+9:third+8+y]
        g3 = find_nth(third,'''/''',5)
        g3=third[g3+1:].replace('_'," ")
        link3='''<a href="'''+third+'''">'''+g3+'''</a>'''

        x=html[first+9:first+150]
        y=(x.find('''?snr='''))
        first=html[first+9:first+8+y]
        g1 = find_nth(first,'''/''',5)
        g1=first[g1+1:].replace('_'," ")
        link1='''<a href="'''+first+'''">'''+g1+'''</a>'''

        x=html[second+9:second+150]
        y=(x.find('''?snr='''))
        second=html[second+9:second+8+y]
        g2 = find_nth(second,'''/''',5)
        g2=second[g2+1:].replace('_'," ")
        link2='''<a href="'''+second+'''">'''+g2+'''</a>'''

        with open('hello2.html', 'r') as file :
          filedata = file.read()


        filedata = filedata.replace('<<<<<firstgame>>>>>', link1)
        filedata = filedata.replace('<<<<<firstgame1>>>>>', link2)
        filedata = filedata.replace('<<<<<firstgame2>>>>>', link3)
        filedata = filedata.replace('google.com', lmao)

        with open('hello2.html', 'w') as file:
          file.write(filedata)

        with open('hello2.html', 'r') as file :
          filedata = file.read()

        print(filedata)

        filedata = filedata.replace(link1,'<<<<<firstgame>>>>>',)
        filedata = filedata.replace(link2,'<<<<<firstgame1>>>>>')
        filedata = filedata.replace(link3,'<<<<<firstgame2>>>>>')
        filedata = filedata.replace(lmao, 'google.com')

        with open('hello2.html', 'w') as file:
          file.write(filedata)
except:
        ho=open('hello3.html','r')
        be=ho.read()
        print(be)
