import requests
import sqlite3
import os
import random
import bs4
import re

if not os.path.exists("jdfresh.sqlite"):
    connection = sqlite3.connect("jdfresh.sqlite")
    cursor = connection.cursor()
    cursor.execute("CREATE TABLE fresh (id INT PRIMARY KEY, sku_name VARCHAR(200), "
                   "price DECIMAL(8,2), class VARCHAR(50), brand VARCHAR(20), prolocation VARCHAR(20), shiplocation VARCHAR(20), prepicnum INT, detpicnum INT)")
else:
    connection = sqlite3.connect("jdfresh.sqlite")
    cursor = connection.cursor()
ua_list = [
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.75.14 (KHTML, like Gecko) Version/7.0.3 Safari/7046A194A",
    "Opera/9.80 (X11; Linux i686; Ubuntu/14.10) Presto/2.12.388 Version/12.16",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:53.0) Gecko/20100101 Firefox/53.0",
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.246"
]


def download(from_url, to_path):
    if os.path.exists(to_path):
        print(".", end="")
        return
    resp = requests.get(from_url, headers={"user-agent": ua_list[random.randint(0, len(ua_list) - 1)]})
    with open(to_path, "wb") as file:
        file.write(resp.content)
        file.close()


def save_product(url):
    resp = requests.get(url, headers={"user-agent": ua_list[random.randint(0, len(ua_list) - 1)]})
    soup = bs4.BeautifulSoup(resp.content, "lxml")
    product_name = str(soup.find("div", "summary-name").h1.text)
    product_class = ""
    try:
        product_class = soup.find("div", "crumbs").find_all("a")[2].text + "@" + \
                        soup.find("div", "crumbs").find_all("a")[
                            1].text
    except IndexError:
        print("BADURL: {url}".format(url=url))
    product_proplace = ""
    product_id = ""
    product_brand = ""
    product_shipplace = ""
    for t in soup.find("table", "zx").find_all("tr"):
        if re.match(".*原产地：.*", t.th.text):
            product_proplace = str(t.td.text)
        elif re.match(".*商品编号：.*", t.th.text):
            product_id = str(t.td.text)
        elif re.match(".*品牌：.*", t.th.text):
            product_brand = str(t.td.text)
        elif re.match(".*发货地：.*", t.th.text):
            product_shipplace = str(t.td.text)
    product_price = str(soup.find("div", "pro-price").find("strong").text)
    counter = 0
    for tag in soup.find("div", "detail-item").find_all("img", "lazy"):
        if not os.path.exists("./fresh_images"):
            os.mkdir("fresh_images")
        if not os.path.exists("./fresh_images/{0}".format(product_id)):
            os.mkdir("fresh_images/{0}".format(product_id))
        download(tag["data-original"], "./fresh_images/{0}/D{1}.jpg".format(product_id, counter))
        counter += 1
    ncounter = 0
    for tag in soup.find("div", "pic-big").find_all("img"):
        if not os.path.exists("./fresh_images"):
            os.mkdir("fresh_images")
        if not os.path.exists("./fresh_images/{0}".format(product_id)):
            os.mkdir("fresh_images/{0}".format(product_id))
        download(tag["src"], "./fresh_images/{0}/P{1}.jpg".format(product_id, ncounter))
        ncounter += 1
    print("({2}){0}: {1}".format(product_name, product_class, product_price))
    print("{0}|{1}|{2}|{3}".format(product_id, product_proplace, product_shipplace, product_brand))
    try:
        cursor.execute(
            "INSERT INTO fresh VALUES ('{id}','{name}','{price}','{fclass}','{brand}','{proloc}','{shiploc}','{prenum}','{detnum}');".format(
                id=product_id,
                name=product_name,
                price=product_price,
                fclass=product_class,
                brand=product_brand,
                proloc=product_proplace,
                shiploc=product_shipplace,
                prenum=ncounter,
                detnum=counter))
    except sqlite3.IntegrityError:
        print("UNIQUE constraint failed")


for j in range(0, 52):
    resp = requests.get(
        "http://www.yiguo.com/productsdata/00_channelhome.html?currentindex={0}&_=1499562979762".format(j),
        headers={"user-agent": ua_list[j % len(ua_list)]})
    soup = bs4.BeautifulSoup(resp.content, "lxml")
    list = soup.find_all("div", "p_name")
    for tag in list:
        if isinstance(tag, bs4.Tag):
            save_product(str(tag.a["href"]))
            connection.commit()
connection.close()
