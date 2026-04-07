# Option Page Resolution

Any MINOVA XML form can be extended at runtime with so called *option pages*. An option page is either a *grid* or a *single-page detail* form,
that is defined in the same way as a normal form -- but without index and only with one page/head. An integrated option page
appears as a normal page in the detail area and is not distinguishable by the user from other pages.

An option page is mostly autonomous and can CRUD its data on its own by using separate procedures. To synchronize the loaded main
detail data set and the option page data set, the primary key(s) of the option page is mapped to the primary key(s) of the main detail.

The addition of option pages is defined in the *Registry* (aka XBS) for the desired form, e.g. to add two for *Item.xml*:

```xml
<node name="Item.xml">
   <map/>
   <node name="OptionPages">
      <map/>
      <node name="ItemTemperatureRange.op.xml">
         <map>
            <entry key="key0" value="KeyLong"/>
         </map>
      </node>
      <node name="ItemTank.op.xml">
         <map>
            <entry key="key0" value="ItemKey"/>
         </map>
      </node>
   </node>
</node>
```

In MINOVA legacy RCP-based applications, the actual addition of option pages was done in the front-end.

Since *April 2026* CAS supports automatic option page addition into the main form's detail before the form code is delivered to the front-end. 
This ability is activated with ```ng.api.dbfiles.resolveforms=true```.

## How it works

As soon as ```ng.api.dbfiles.resolveforms=true``` is activated in the (µ)CAS and a form, e.g. *Item.xml*, is requested:

- CAS checks the Registry (*tRegistry*) to find whether option pages are defined for the requested form
- If found option pages are fetched from *tFile* and integrated into the main form's XML code

**Example Registry entries**
```xml
<node name="Item.xml">
   <map/>
   <node name="OptionPages">
      <map/>
      <node name="ItemTemperatureRange.op.xml">
         <map>
            <entry key="key0" value="KeyLong"/>
         </map>
      </node>
      <node name="ItemTank.op.xml">
         <map>
            <entry key="key0" value="ItemKey"/>
         </map>
      </node>
   </node>
</node>
```

**Item.xml *before* change**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<form icon="Item.ico" title="@tItem.Administration">
	<index-view id="IndexView" index-report="auto" source="dbo.vItemIndex">
		<column key="true" name="KeyLong" size="0" text="@KeyLong">
			<number/>
		</column>
		...
	</index-view>
	<detail button-block-visible="true" id="Detail" procedure-suffix="Item" button-copy-visible="true">
		<head>
			<field key-type="primary" name="KeyLong" sql-index="0" visible="false">
				<number/>
			</field>
			...
		</head>
		...
		<page icon="CustomsPositionHistory" id="CustomsPositionHistory" text="@tItem.Group.CustomsPositionHistory">
			...
		</page>
	</detail>
</form>
```

**ItemTemperatureRange.op.xml**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<form icon="ItemTemperatureRange.ico" title="@tItem.Group.TemperatureRange">
	<detail id="Detail" procedure-suffix="ItemTemperatureRange">
		<head>
			<field key-type="primary" name="KeyLong" sql-index="0" visible="false">
				<number/>
			</field>
			<field name="MinimumTemperature" number-columns-spanned="4" sql-index="1" text="@tItem.MinimumTemperature">
				<number decimals="1"/>
			</field>
			<field name="MaximumTemperature" number-columns-spanned="4" sql-index="2" text="@tItem.MaximumTemperature">
				<number decimals="1"/>
			</field>
		</head>
	</detail>
</form>
```

**ItemTank.op.xml**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<form  icon="Tank.ico" title="@tTank">
	<detail procedure-suffix="ItemTank">
		<head>
			<field key-type="primary" name="ItemKey" sql-index="0" visible="false">
				<number/>
			</field>
			<field name="TankID" sql-index="1" text="@Description">
				<text length="10"/>
			</field>
		</head>
	</detail>
</form>
```

CAS collapses the *form/detail/page* (or *form/detail/head*) to a single *optionpage* node with all required attributes.
Furthermore, the corresponding key-fields within the optionpages also receive *map-to* attributes that point to the corresponding
main detail's primary fields. That is, the UI has all required information within the XML code alone to make the options pages functioning.

**Resulting Item.xml**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<form icon="Item.ico" title="@tItem.Administration" >
	<index-view id="IndexView" index-report="auto" source="dbo.vItemIndex">
		...
	</index-view>
	<detail button-block-visible="true" id="Detail" procedure-suffix="Item" button-copy-visible="true">
		<head>
			<field key-type="primary" name="KeyLong" sql-index="0" visible="false">
				<number/>
			</field>
			...
		</head>
		...
		<page icon="CustomsPositionHistory" id="CustomsPositionHistory" text="@tItem.Group.CustomsPositionHistory">
			...
		</page>
    <optionpage icon="Tank.ico" procedure-suffix="ItemTank" text="Tank">
      <field key-type="primary" map-to="KeyLong" name="ItemKey" sql-index="0" visible="false">
        <number/>
      </field>
      <field name="TankID" sql-index="1" text="Beschreibung">
        <text length="10"/>
      </field>
    </optionpage>
    <optionpage icon="ItemTemperatureRange.ico" procedure-suffix="ItemTemperatureRange" text="Temperaturbereich">
      <field key-type="primary" map-to="KeyLong" name="KeyLong" sql-index="0" visible="false">
        <number/>
      </field>
      <field name="MinimumTemperature" number-columns-spanned="4" sql-index="1" text="min. Temp.">
        <number decimals="1"/>
      </field>
      <field name="MaximumTemperature" number-columns-spanned="4" sql-index="2" text="max. Temp.">
        <number decimals="1"/>
      </field>
    </optionpage>
	</detail>
</form>
```



